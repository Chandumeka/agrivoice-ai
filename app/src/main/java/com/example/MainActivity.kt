package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AgriVoiceApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AgriVoiceApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val allFarms by viewModel.allFarms.collectAsState()
    val selectedFarm by viewModel.selectedFarm.collectAsState()
    val currentCropCycle by viewModel.currentCropCycle.collectAsState()
    val currentSoilReport by viewModel.currentSoilReport.collectAsState()
    val allAlerts by viewModel.allAlerts.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val marketPrices by viewModel.marketPrices.collectAsState()
    val aiMemories by viewModel.aiMemories.collectAsState()
    val expertReviews by viewModel.expertReviews.collectAsState()
    val communityPosts by viewModel.communityPosts.collectAsState()

    val voiceState by viewModel.voiceState.collectAsState()
    val transcription by viewModel.currentTranscription.collectAsState()
    val audioLevels by viewModel.liveAudioLevels.collectAsState()
    val isBriefingPlaying by viewModel.isBriefingPlaying.collectAsState()

    val isScanningImage by viewModel.isScanningImage.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val diseaseResult by viewModel.diseaseResult.collectAsState()

    val simAcres by viewModel.simAcres.collectAsState()
    val simYield by viewModel.simExpectedYieldPerAcre.collectAsState()
    val simCost by viewModel.simCostPerAcre.collectAsState()
    val simPriceMin by viewModel.simPriceMin.collectAsState()
    val simPriceMax by viewModel.simPriceMax.collectAsState()
    val profitCalc by viewModel.profitCalculation.collectAsState()

    var showSplashScreen by remember { mutableStateOf(true) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showSplashScreen) {
        SplashScreen(
            onSplashFinished = {
                showSplashScreen = false
            }
        )
        return
    }

    Scaffold(
        topBar = {
            if (currentScreen != NavigationScreen.ONBOARDING) {
                AgriTopBar(
                    profile = profile,
                    selectedFarm = selectedFarm,
                    allFarms = allFarms,
                    currentScreen = currentScreen,
                    onFarmSelected = { farmId -> viewModel.switchFarm(farmId) },
                    onLanguageClick = { showLanguageDialog = true },
                    onNavigateBack = { viewModel.navigateTo(NavigationScreen.HOME) },
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) }
                )
            }
        },
        bottomBar = {
            if (currentScreen != NavigationScreen.ONBOARDING) {
                AgriBottomBar(
                    currentScreen = currentScreen,
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) },
                    onVoiceClick = {
                        showVoiceDialog = true
                        viewModel.startVoiceListening()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavigationScreen.HOME -> HomeScreen(
                    profile = profile,
                    selectedFarm = selectedFarm,
                    cropCycle = currentCropCycle,
                    alerts = allAlerts,
                    marketPrices = marketPrices,
                    isBriefingPlaying = isBriefingPlaying,
                    onPlayBriefing = { viewModel.playMorningBriefing() },
                    onStopBriefing = { viewModel.stopVoice() },
                    onOpenVoice = {
                        showVoiceDialog = true
                        viewModel.startVoiceListening()
                    },
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) },
                    onMarkAlertRead = { alertId -> viewModel.markAlertRead(alertId) }
                )
                NavigationScreen.MY_FARMS -> MyFarmsScreen(
                    farms = allFarms,
                    selectedFarm = selectedFarm,
                    onSelectFarm = { farmId -> viewModel.switchFarm(farmId) },
                    onDeleteFarm = { farmId -> viewModel.deleteFarm(farmId) },
                    onAddNewFarm = { name, acres, crop, soil, loc ->
                        viewModel.addNewFarm(name, acres, crop, soil, loc)
                    }
                )
                NavigationScreen.CROP_SCANNER -> CropScannerScreen(
                    isScanning = isScanningImage,
                    scanProgress = scanProgress,
                    analysisResult = diseaseResult,
                    onScanImage = { viewModel.runPlantScan(null) },
                    onResetScanner = { viewModel.resetScanner() },
                    onRequestExpertReview = { issue, prelim, conf ->
                        viewModel.requestExpertEscalation(issue, prelim, conf)
                    }
                )
                NavigationScreen.WEATHER -> WeatherScreen()
                NavigationScreen.IRRIGATION -> IrrigationScreen(
                    farm = selectedFarm,
                    cropCycle = currentCropCycle
                )
                NavigationScreen.SOIL -> SoilScreen(soilReport = currentSoilReport)
                NavigationScreen.MARKET -> MarketScreen(
                    marketPrices = marketPrices,
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) }
                )
                NavigationScreen.PROFIT_SIMULATOR -> ProfitSimulatorScreen(
                    acres = simAcres,
                    yieldPerAcre = simYield,
                    costPerAcre = simCost,
                    priceMin = simPriceMin,
                    priceMax = simPriceMax,
                    profitCalc = profitCalc,
                    onUpdateInputs = { a, y, c, pMin, pMax ->
                        viewModel.updateSimulatorInputs(a, y, c, pMin, pMax)
                    }
                )
                NavigationScreen.CROP_CALENDAR -> CropCalendarScreen(cropCycle = currentCropCycle)
                NavigationScreen.FARM_MAP -> FarmMapScreen(selectedFarm = selectedFarm)
                NavigationScreen.ALERTS -> AlertsScreen(
                    alerts = allAlerts,
                    onMarkRead = { alertId -> viewModel.markAlertRead(alertId) }
                )
                NavigationScreen.EXPERT_REVIEW -> ExpertReviewScreen(expertReviews = expertReviews)
                NavigationScreen.COMMUNITY -> CommunityScreen(communityPosts = communityPosts)
                NavigationScreen.AI_MEMORY -> AIMemoryScreen(
                    memories = aiMemories,
                    onToggleMemory = { mem -> viewModel.toggleMemory(mem) },
                    onDeleteMemory = { memId -> viewModel.deleteMemory(memId) },
                    onAddMemory = { fact, cat -> viewModel.addAIMemory(fact, cat) }
                )
                NavigationScreen.PROFILE -> ProfileScreen(
                    profile = profile,
                    onLanguageClick = { showLanguageDialog = true },
                    onToggleDemoMode = { viewModel.toggleDemoMode() },
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) }
                )
                NavigationScreen.ADMIN_DASHBOARD -> AdminDashboardScreen()
                NavigationScreen.ONBOARDING -> OnboardingScreen(
                    onCompleteOnboarding = { name, lang, loc, acres, crop ->
                        viewModel.setLanguage(lang)
                        viewModel.navigateTo(NavigationScreen.HOME)
                    }
                )
                else -> HomeScreen(
                    profile = profile,
                    selectedFarm = selectedFarm,
                    cropCycle = currentCropCycle,
                    alerts = allAlerts,
                    marketPrices = marketPrices,
                    isBriefingPlaying = isBriefingPlaying,
                    onPlayBriefing = { viewModel.playMorningBriefing() },
                    onStopBriefing = { viewModel.stopVoice() },
                    onOpenVoice = {
                        showVoiceDialog = true
                        viewModel.startVoiceListening()
                    },
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) },
                    onMarkAlertRead = { alertId -> viewModel.markAlertRead(alertId) }
                )
            }
        }
    }

    // Voice Assistant Sheet
    if (showVoiceDialog) {
        VoiceAssistantDialog(
            voiceState = voiceState,
            transcription = transcription,
            audioLevels = audioLevels,
            chatMessages = chatMessages,
            currentLangCode = profile?.languageCode ?: "en",
            onStartListening = { viewModel.startVoiceListening() },
            onSubmitQuery = { query -> viewModel.submitVoiceQuery(query) },
            onStopVoice = { viewModel.stopVoice() },
            onDismiss = {
                showVoiceDialog = false
                viewModel.stopVoice()
            },
            onLanguageClick = { showLanguageDialog = true }
        )
    }

    // Language Selection Sheet
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLangCode = profile?.languageCode ?: "en",
            onSelectLanguage = { langCode ->
                viewModel.setLanguage(langCode)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}
