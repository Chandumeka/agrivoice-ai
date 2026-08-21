package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ai.AgriAgentOrchestrator
import com.example.ai.DiseaseAnalysisResult
import com.example.data.local.AgriVoiceDatabase
import com.example.data.model.*
import com.example.data.repository.AgriVoiceRepository
import com.example.util.LanguageManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class VoiceUiState {
    IDLE,
    LISTENING,
    PROCESSING,
    RESPONDING,
    ERROR
}

enum class NavigationScreen {
    HOME,
    MY_FARMS,
    VOICE_ASSISTANT,
    CROP_SCANNER,
    WEATHER,
    IRRIGATION,
    SOIL,
    MARKET,
    PROFIT_SIMULATOR,
    CROP_CALENDAR,
    FARM_MAP,
    ALERTS,
    EXPERT_REVIEW,
    COMMUNITY,
    AI_MEMORY,
    PROFILE,
    ADMIN_DASHBOARD,
    ONBOARDING
}

data class ProfitCalculation(
    val grossRevenueMin: Double,
    val grossRevenueMax: Double,
    val totalCost: Double,
    val netProfitMin: Double,
    val netProfitMax: Double,
    val breakEvenYieldQuintals: Double,
    val roiPercentMin: Double,
    val roiPercentMax: Double
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AgriVoiceDatabase::class.java,
        "agrivoice_db"
    ).fallbackToDestructiveMigration().build()

    val repository = AgriVoiceRepository(db)
    val orchestrator = AgriAgentOrchestrator(application)

    // Navigation State
    private val _currentScreen = MutableStateFlow(NavigationScreen.HOME)
    val currentScreen: StateFlow<NavigationScreen> = _currentScreen.asStateFlow()

    // Data Flows
    val profile: StateFlow<FarmerProfile?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allFarms: StateFlow<List<Farm>> = repository.allFarms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedFarm: StateFlow<Farm?> = repository.selectedFarm
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allAlerts: StateFlow<List<FarmAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val marketPrices: StateFlow<List<MarketPrice>> = repository.marketPrices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aiMemories: StateFlow<List<AIMemory>> = repository.aiMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expertReviews: StateFlow<List<ExpertReview>> = repository.expertReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityPosts: StateFlow<List<CommunityPost>> = repository.communityPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Farm Crop Cycle
    val currentCropCycle: StateFlow<CropCycle?> = selectedFarm.flatMapLatest { farm ->
        if (farm != null) repository.getCropCycle(farm.id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Selected Farm Soil Report
    val currentSoilReport: StateFlow<SoilReport?> = selectedFarm.flatMapLatest { farm ->
        if (farm != null) repository.getSoilReport(farm.id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Voice & Chat State
    private val _voiceState = MutableStateFlow(VoiceUiState.IDLE)
    val voiceState: StateFlow<VoiceUiState> = _voiceState.asStateFlow()

    private val _currentTranscription = MutableStateFlow("")
    val currentTranscription: StateFlow<String> = _currentTranscription.asStateFlow()

    private val _liveAudioLevels = MutableStateFlow(listOf(0.2f, 0.4f, 0.7f, 0.5f, 0.8f, 0.3f, 0.6f))
    val liveAudioLevels: StateFlow<List<Float>> = _liveAudioLevels.asStateFlow()

    private val _isBriefingPlaying = MutableStateFlow(false)
    val isBriefingPlaying: StateFlow<Boolean> = _isBriefingPlaying.asStateFlow()

    // Scanner State
    private val _isScanningImage = MutableStateFlow(false)
    val isScanningImage: StateFlow<Boolean> = _isScanningImage.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _diseaseResult = MutableStateFlow<DiseaseAnalysisResult?>(null)
    val diseaseResult: StateFlow<DiseaseAnalysisResult?> = _diseaseResult.asStateFlow()

    // Profit Simulator Inputs
    private val _simCrop = MutableStateFlow("Tomato")
    val simCrop: StateFlow<String> = _simCrop.asStateFlow()

    private val _simAcres = MutableStateFlow(2.5)
    val simAcres: StateFlow<Double> = _simAcres.asStateFlow()

    private val _simExpectedYieldPerAcre = MutableStateFlow(80.0) // Quintals
    val simExpectedYieldPerAcre: StateFlow<Double> = _simExpectedYieldPerAcre.asStateFlow()

    private val _simCostPerAcre = MutableStateFlow(45000.0) // INR (Seeds, Fertilizers, Labor)
    val simCostPerAcre: StateFlow<Double> = _simCostPerAcre.asStateFlow()

    private val _simPriceMin = MutableStateFlow(2400.0) // INR per Quintal
    val simPriceMin: StateFlow<Double> = _simPriceMin.asStateFlow()

    private val _simPriceMax = MutableStateFlow(3200.0) // INR per Quintal
    val simPriceMax: StateFlow<Double> = _simPriceMax.asStateFlow()

    // Derived Profit calculation
    val profitCalculation: StateFlow<ProfitCalculation> = combine(
        _simAcres, _simExpectedYieldPerAcre, _simCostPerAcre, _simPriceMin, _simPriceMax
    ) { acres, yieldPerAcre, costPerAcre, priceMin, priceMax ->
        val totalYield = acres * yieldPerAcre
        val totalCost = acres * costPerAcre
        val grossMin = totalYield * priceMin
        val grossMax = totalYield * priceMax
        val netMin = grossMin - totalCost
        val netMax = grossMax - totalCost
        val breakEvenYield = if (priceMin > 0) totalCost / priceMin else 0.0
        val roiMin = if (totalCost > 0) (netMin / totalCost) * 100 else 0.0
        val roiMax = if (totalCost > 0) (netMax / totalCost) * 100 else 0.0

        ProfitCalculation(
            grossRevenueMin = grossMin,
            grossRevenueMax = grossMax,
            totalCost = totalCost,
            netProfitMin = netMin,
            netProfitMax = netMax,
            breakEvenYieldQuintals = breakEvenYield,
            roiPercentMin = roiMin,
            roiPercentMax = roiMax
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfitCalculation(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0))

    // Offline / Sync Indicator
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var audioAnimJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializePreSeededDataIfEmpty()
        }
    }

    fun navigateTo(screen: NavigationScreen) {
        _currentScreen.value = screen
    }

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            val current = profile.value ?: return@launch
            repository.updateProfile(current.copy(languageCode = langCode))
        }
    }

    fun switchFarm(farmId: String) {
        viewModelScope.launch {
            repository.selectFarm(farmId)
        }
    }

    fun toggleDemoMode() {
        viewModelScope.launch {
            val current = profile.value ?: return@launch
            repository.updateProfile(current.copy(isDemoMode = !current.isDemoMode))
        }
    }

    fun startVoiceListening() {
        _voiceState.value = VoiceUiState.LISTENING
        _currentTranscription.value = "Listening to your farm question in ${LanguageManager.getLanguage(profile.value?.languageCode ?: "en").name}..."

        audioAnimJob?.cancel()
        audioAnimJob = viewModelScope.launch {
            while (_voiceState.value == VoiceUiState.LISTENING) {
                _liveAudioLevels.value = List(7) { (0.15f + Math.random().toFloat() * 0.85f) }
                delay(120)
            }
        }
    }

    fun submitVoiceQuery(query: String) {
        audioAnimJob?.cancel()
        _voiceState.value = VoiceUiState.PROCESSING
        _currentTranscription.value = query

        viewModelScope.launch {
            val lang = profile.value?.languageCode ?: "en"

            // Save user message
            val userMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = "user",
                text = query,
                languageCode = lang,
                isVoiceInput = true
            )
            repository.addChatMessage(userMsg)

            delay(600) // Realistic multi-agent reasoning delay

            val agentResponse = orchestrator.processUserQuery(
                query = query,
                profile = profile.value,
                selectedFarm = selectedFarm.value,
                cropCycle = currentCropCycle.value,
                soilReport = currentSoilReport.value,
                marketPrices = marketPrices.value,
                memories = aiMemories.value,
                languageCode = lang
            )

            val aiMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = "assistant",
                text = agentResponse.text,
                specialistAgent = agentResponse.specialistAgent,
                languageCode = lang,
                confidence = agentResponse.confidence,
                dataSource = agentResponse.dataSource
            )
            repository.addChatMessage(aiMsg)

            _voiceState.value = VoiceUiState.RESPONDING

            if (profile.value?.autoVoiceResponse == true) {
                orchestrator.speak(agentResponse.text, lang) {
                    _voiceState.value = VoiceUiState.IDLE
                }
            } else {
                _voiceState.value = VoiceUiState.IDLE
            }
        }
    }

    fun stopVoice() {
        audioAnimJob?.cancel()
        orchestrator.stopSpeaking()
        _voiceState.value = VoiceUiState.IDLE
        _isBriefingPlaying.value = false
    }

    fun playMorningBriefing() {
        val lang = profile.value?.languageCode ?: "en"
        val farm = selectedFarm.value
        val crop = currentCropCycle.value

        val briefingText = when (lang) {
            "hi" -> "शुभ प्रभात रमेश जी! आज आपके ${farm?.name ?: "खेत"} में टमाटर की फसल अच्छी स्थिति में है। कल दोपहर 18mm बारिश की संभावना है, इसलिए आज शाम की ड्रिप सिंचाई रोक दें। कोलार मंडी में आज टमाटर ₹2,850 प्रति क्विंटल के मजबूत भाव पर खुला है।"
            "kn" -> "ಶುಭೋದಯ ರಮೇಶ್ ಅವರೇ! ಇಂದಿನ ನಿಮ್ಮ ${farm?.name ?: "ಕೃಷಿ"}ಯಲ್ಲಿ ಟೊಮೆಟೊ ಬೆಳೆ ಉತ್ತಮ ಸ್ಥಿತಿಯಲ್ಲಿದೆ. ನಾಳೆ 18mm ಮಳೆ ನಿರೀಕ್ಷೆಯಿರುವುದರಿಂದ ಇಂದು ನೀರಾವರಿ ನಿಲ್ಲಿಸಿ. ಕೋಲಾರ ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ ಕ್ವಿಂಟಾಲ್‌ಗೆ ₹2,850 ಬೆಲೆ ಇದೆ."
            else -> "Good morning Ramesh! Today in ${farm?.name ?: "Farm A"}, your ${crop?.cropName ?: "Tomato"} crop is healthy at Day ${crop?.daysSincePlanting ?: 48}. With 18mm rainfall forecast tomorrow, irrigation can be skipped today. Tomato price in Kolar Mandi is up 12% at ₹2,850 per quintal. Have a productive day!"
        }

        _isBriefingPlaying.value = true
        orchestrator.speak(briefingText, lang) {
            _isBriefingPlaying.value = false
        }
    }

    fun runPlantScan(bitmap: Bitmap?) {
        viewModelScope.launch {
            _isScanningImage.value = true
            _scanProgress.value = 0f

            for (i in 1..10) {
                delay(120)
                _scanProgress.value = i / 10f
            }

            val result = orchestrator.analyzeCropImage(bitmap)
            _diseaseResult.value = result
            _isScanningImage.value = false
        }
    }

    fun resetScanner() {
        _diseaseResult.value = null
        _isScanningImage.value = false
        _scanProgress.value = 0f
    }

    fun requestExpertEscalation(issue: String, preliminary: String, confidence: Int) {
        viewModelScope.launch {
            val review = ExpertReview(
                id = UUID.randomUUID().toString(),
                farmId = selectedFarm.value?.id ?: "farm_a",
                cropName = currentCropCycle.value?.cropName ?: "Tomato",
                question = issue,
                aiPreliminaryDiagnosis = preliminary,
                aiConfidencePercent = confidence,
                status = "Submitted",
                expertName = "Agricultural Extension Officer Assigned",
                expertResponse = "Request submitted to district agricultural scientist. Response expected within 4 hours."
            )
            repository.submitExpertReview(review)
            navigateTo(NavigationScreen.EXPERT_REVIEW)
        }
    }

    fun updateSimulatorInputs(acres: Double, yield: Double, cost: Double, priceMin: Double, priceMax: Double) {
        _simAcres.value = acres
        _simExpectedYieldPerAcre.value = yield
        _simCostPerAcre.value = cost
        _simPriceMin.value = priceMin
        _simPriceMax.value = priceMax
    }

    fun addNewFarm(name: String, acres: Double, crop: String, soil: String, location: String) {
        viewModelScope.launch {
            val farmId = "farm_" + UUID.randomUUID().toString().take(6)
            val newFarm = Farm(
                id = farmId,
                name = name,
                sizeAcres = acres,
                mainCrop = crop,
                soilType = soil,
                location = location,
                healthScore = 90,
                healthStatus = "Healthy",
                isSelected = true
            )
            val newCycle = CropCycle(
                id = "cycle_" + UUID.randomUUID().toString().take(6),
                farmId = farmId,
                cropName = crop,
                variety = "Certified Hybrid Variety",
                currentStage = "Germination Stage",
                stageProgressPercent = 15,
                daysSincePlanting = 10,
                totalEstimatedDays = 120,
                targetYieldQuintals = acres * 75.0,
                plantingDate = "10 Aug 2026",
                harvestDate = "10 Dec 2026",
                currentAdvisory = "Ensure uniform soil moisture during root establishment. Avoid heavy fertilizer right now."
            )
            repository.addFarm(newFarm, newCycle)
            repository.selectFarm(farmId)
            navigateTo(NavigationScreen.HOME)
        }
    }

    fun deleteFarm(farmId: String) {
        viewModelScope.launch {
            repository.deleteFarm(farmId)
            val first = allFarms.value.firstOrNull { it.id != farmId }
            if (first != null) {
                repository.selectFarm(first.id)
            }
        }
    }

    fun addAIMemory(fact: String, category: String) {
        viewModelScope.launch {
            repository.addMemory(
                AIMemory(
                    id = UUID.randomUUID().toString(),
                    category = category,
                    fact = fact,
                    isEnabled = true
                )
            )
        }
    }

    fun toggleMemory(memory: AIMemory) {
        viewModelScope.launch {
            repository.updateMemory(memory.copy(isEnabled = !memory.isEnabled))
        }
    }

    fun deleteMemory(memoryId: String) {
        viewModelScope.launch {
            repository.deleteMemory(memoryId)
        }
    }

    fun markAlertRead(alertId: String) {
        viewModelScope.launch {
            repository.markAlertRead(alertId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        orchestrator.release()
    }
}
