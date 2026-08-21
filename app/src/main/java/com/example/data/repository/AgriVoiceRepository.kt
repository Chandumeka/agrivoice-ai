package com.example.data.repository

import com.example.data.local.AgriVoiceDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class AgriVoiceRepository(private val database: AgriVoiceDatabase) {

    val profile: Flow<FarmerProfile?> = database.farmerDao().getProfile()
    val allFarms: Flow<List<Farm>> = database.farmDao().getAllFarms()
    val selectedFarm: Flow<Farm?> = database.farmDao().getSelectedFarm()
    val allAlerts: Flow<List<FarmAlert>> = database.alertDao().getAllAlerts()
    val chatMessages: Flow<List<ChatMessage>> = database.chatDao().getAllMessages()
    val marketPrices: Flow<List<MarketPrice>> = database.marketDao().getAllMarketPrices()
    val aiMemories: Flow<List<AIMemory>> = database.memoryDao().getAllMemories()
    val expertReviews: Flow<List<ExpertReview>> = database.expertDao().getAllReviews()
    val communityPosts: Flow<List<CommunityPost>> = database.communityDao().getAllPosts()

    fun getCropCycle(farmId: String): Flow<CropCycle?> = database.cropDao().getCropCycle(farmId)
    fun getSoilReport(farmId: String): Flow<SoilReport?> = database.soilDao().getLatestSoilReport(farmId)

    suspend fun initializePreSeededDataIfEmpty() {
        val existingProfile = database.farmerDao().getProfile().firstOrNull()
        if (existingProfile == null) {
            // Seed profile
            database.farmerDao().insertOrUpdateProfile(
                FarmerProfile(
                    id = "primary_farmer",
                    name = "Ramesh Kumar",
                    languageCode = "en",
                    location = "Kolar District, Karnataka",
                    phone = "+91 98765 43210",
                    experienceYears = 14,
                    preferredUnits = "Acres / Quintal",
                    isDemoMode = true,
                    hasCompletedOnboarding = true,
                    voiceSpeed = 1.0f,
                    autoVoiceResponse = true,
                    lastSyncTimestamp = System.currentTimeMillis() - 1000 * 60 * 45 // 45 mins ago
                )
            )

            // Seed Farms
            val farms = listOf(
                Farm(
                    id = "farm_a",
                    name = "Farm A - North Plot",
                    sizeAcres = 2.5,
                    mainCrop = "Tomato (Arka Rakshak)",
                    soilType = "Red Sandy Loam",
                    location = "Kolar East, Sector 3",
                    healthScore = 88,
                    healthStatus = "Healthy",
                    irrigationType = "Drip Irrigation with Timer",
                    isSelected = true
                ),
                Farm(
                    id = "farm_b",
                    name = "Farm B - River Valley",
                    sizeAcres = 4.0,
                    mainCrop = "Paddy (BPT 5204)",
                    soilType = "Clay Loam",
                    location = "Kolar Basin, Plot 12",
                    healthScore = 74,
                    healthStatus = "Watch",
                    irrigationType = "Canal & Tube Well",
                    isSelected = false
                ),
                Farm(
                    id = "farm_c",
                    name = "Farm C - Hillside Parcel",
                    sizeAcres = 1.5,
                    mainCrop = "Chilli (Byadgi Variety)",
                    soilType = "Red Loamy Soil",
                    location = "North Ridge, Plot 7",
                    healthScore = 65,
                    healthStatus = "Attention",
                    irrigationType = "Sprinkler System",
                    isSelected = false
                )
            )
            database.farmDao().insertFarms(farms)

            // Seed Crop Cycles
            val cropCycles = listOf(
                CropCycle(
                    id = "cycle_tomato",
                    farmId = "farm_a",
                    cropName = "Tomato",
                    variety = "Arka Rakshak F1",
                    currentStage = "Flowering & Fruit Setting",
                    stageProgressPercent = 65,
                    daysSincePlanting = 48,
                    totalEstimatedDays = 110,
                    targetYieldQuintals = 220.0,
                    plantingDate = "03 Jul 2026",
                    harvestDate = "25 Sep 2026",
                    currentAdvisory = "Nutrient uptake is high. Maintain optimal soil moisture and apply calcium-boron foliar spray to prevent blossom end rot."
                ),
                CropCycle(
                    id = "cycle_paddy",
                    farmId = "farm_b",
                    cropName = "Paddy",
                    variety = "BPT 5204 (Samba Mahsuri)",
                    currentStage = "Tillering Stage",
                    stageProgressPercent = 40,
                    daysSincePlanting = 32,
                    totalEstimatedDays = 135,
                    targetYieldQuintals = 160.0,
                    plantingDate = "19 Jul 2026",
                    harvestDate = "30 Nov 2026",
                    currentAdvisory = "Keep shallow water layer of 2-3 cm. Watch for leaf folder pests in humid mornings."
                ),
                CropCycle(
                    id = "cycle_chilli",
                    farmId = "farm_c",
                    cropName = "Chilli",
                    variety = "Byadgi Kaddi",
                    currentStage = "Vegetative Growth",
                    stageProgressPercent = 35,
                    daysSincePlanting = 28,
                    totalEstimatedDays = 150,
                    targetYieldQuintals = 45.0,
                    plantingDate = "23 Jul 2026",
                    harvestDate = "20 Dec 2026",
                    currentAdvisory = "Thrips activity reported in neighboring farms. Inspect underside of leaves and set up blue sticky traps."
                )
            )
            database.cropDao().insertCropCycles(cropCycles)

            // Seed Alerts
            val alerts = listOf(
                FarmAlert(
                    id = "alert_1",
                    farmId = "farm_a",
                    title = "Moderate Rain Forecast Tomorrow (18mm)",
                    message = "Expected precipitation in Kolar. Drip irrigation can be paused for the next 36 hours.",
                    severity = "MEDIUM",
                    category = "WEATHER",
                    actionableStep = "Pause drip line cycle today evening to prevent root waterlogging.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 30
                ),
                FarmAlert(
                    id = "alert_2",
                    farmId = "farm_c",
                    title = "High Humidity Alert: Disease Vulnerability",
                    message = "Morning humidity 89% with cloudy skies increases risk of Early Blight & Downy Mildew.",
                    severity = "HIGH",
                    category = "DISEASE",
                    actionableStep = "Take photo scan of lower leaves using Crop Scanner to inspect spots.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120
                ),
                FarmAlert(
                    id = "alert_3",
                    farmId = "farm_a",
                    title = "Tomato Mandi Price Spurt (+12%)",
                    message = "Kolar APMC Market price rose to ₹2,850/Quintal due to high interstate demand.",
                    severity = "LOW",
                    category = "MARKET",
                    actionableStep = "Evaluate harvesting Grade-A fruits 2 days earlier if firmness allows.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 240
                )
            )
            database.alertDao().insertAlerts(alerts)

            // Seed Soil Reports
            val soilReports = listOf(
                SoilReport(
                    id = "soil_farm_a",
                    farmId = "farm_a",
                    testDate = "15 Jun 2026",
                    ph = 6.8,
                    nitrogenKgPerHa = 265.0, // Medium
                    phosphorusKgPerHa = 34.0, // High
                    potassiumKgPerHa = 310.0, // High
                    organicCarbonPercent = 0.62,
                    electricalConductivity = 0.42,
                    healthSummary = "Balanced pH and rich potassium reserve. Ideal for solanaceous crops like tomato.",
                    recommendedActions = "Supplement with organic compost (FYM) to boost organic carbon above 0.75%."
                ),
                SoilReport(
                    id = "soil_farm_b",
                    farmId = "farm_b",
                    testDate = "10 Jun 2026",
                    ph = 7.2,
                    nitrogenKgPerHa = 210.0,
                    phosphorusKgPerHa = 22.0,
                    potassiumKgPerHa = 180.0,
                    organicCarbonPercent = 0.55,
                    electricalConductivity = 0.38,
                    healthSummary = "Heavy clay soil with good water retention. Mild nitrogen replenishment needed.",
                    recommendedActions = "Apply neem-coated urea in split doses during active tillering."
                ),
                SoilReport(
                    id = "soil_farm_c",
                    farmId = "farm_c",
                    testDate = "22 Jun 2026",
                    ph = 6.2,
                    nitrogenKgPerHa = 195.0,
                    phosphorusKgPerHa = 18.0,
                    potassiumKgPerHa = 160.0,
                    organicCarbonPercent = 0.48,
                    electricalConductivity = 0.29,
                    healthSummary = "Slightly acidic. Moderate micronutrient deficiency (Zinc, Boron).",
                    recommendedActions = "Incorporate agricultural lime @ 150 kg/acre and apply zinc sulphate."
                )
            )
            database.soilDao().insertSoilReports(soilReports)

            // Seed Market Prices
            val prices = listOf(
                MarketPrice("m1", "Tomato", "Kolar APMC Mandi", "Kolar", "Karnataka", 2850.0, 2400.0, 3100.0, "UP", 12.4, 8, 45.0),
                MarketPrice("m2", "Tomato", "Azadpur Mandi", "New Delhi", "Delhi", 3200.0, 2700.0, 3600.0, "UP", 8.5, 2150, 480.0),
                MarketPrice("m3", "Tomato", "Bangalore Yashwanthpur", "Bangalore Urban", "Karnataka", 2920.0, 2500.0, 3250.0, "UP", 6.2, 65, 95.0),
                MarketPrice("m4", "Paddy (Samba)", "Shimoga APMC", "Shimoga", "Karnataka", 2450.0, 2300.0, 2600.0, "STABLE", 0.5, 280, 160.0),
                MarketPrice("m5", "Chilli (Byadgi)", "Byadgi APMC", "Haveri", "Karnataka", 18500.0, 16000.0, 21000.0, "UP", 14.8, 340, 220.0),
                MarketPrice("m6", "Cotton", "Guntur Mandi", "Guntur", "Andhra Pradesh", 7350.0, 6900.0, 7800.0, "DOWN", -2.1, 460, 290.0),
                MarketPrice("m7", "Onion", "Lasalgaon Mandi", "Nashik", "Maharashtra", 1950.0, 1500.0, 2300.0, "DOWN", -5.4, 890, 410.0)
            )
            database.marketDao().insertMarketPrices(prices)

            // Seed AI Memories
            val memories = listOf(
                AIMemory("mem_1", "Farm Setup", "Ramesh manages 3 farms totaling 8 acres across Kolar district.", true),
                AIMemory("mem_2", "Preferences", "Prefers drip irrigation and organic bio-fertilizer alternatives where feasible.", true),
                AIMemory("mem_3", "Market Goal", "Focuses on delivering Grade-A tomatoes to local Kolar Mandi within 10 km radius.", true),
                AIMemory("mem_4", "Language", "Comfortable with English and Kannada voice queries.", true)
            )
            database.memoryDao().insertMemories(memories)

            // Seed Initial Chat Welcome
            val initialMessages = listOf(
                ChatMessage(
                    id = "msg_welcome",
                    role = "assistant",
                    text = "Namaste Ramesh! I am AgriVoice AI, your personal farming assistant. Your Tomato crop in Farm A is in active flowering (Day 48). Weather in Kolar is partly cloudy (27°C) with 18mm rain forecast tomorrow. Drip irrigation can be paused today. How can I help your farm today?",
                    specialistAgent = "Orchestrator",
                    languageCode = "en",
                    confidence = 0.96f,
                    dataSource = "Farm A Sensor Grid & IMD Meteorological Radar"
                )
            )
            database.chatDao().insertMessages(initialMessages)

            // Seed Community Posts
            val posts = listOf(
                CommunityPost(
                    id = "post_1",
                    authorName = "Dr. S. K. Manjunath",
                    authorLocation = "ICAR Agricultural Research Station",
                    cropTag = "Tomato Advisory",
                    content = "Farmers in Kolar & Chikkaballapur: Due to sudden cloud cover and 85%+ humidity, spray Trichoderma viride or copper oxychloride (2.5g/L) as a prophylactic guard against early blight.",
                    likesCount = 48,
                    commentsCount = 12,
                    isVerifiedExpert = true,
                    isOfficialAdvisory = true
                ),
                CommunityPost(
                    id = "post_2",
                    authorName = "Venkatesh Gowda",
                    authorLocation = "Malur, Kolar",
                    cropTag = "Market Observation",
                    content = "Tomato rates at Kolar APMC reached ₹650 per 15kg crate this morning. Demand from Tamil Nadu and Kerala buyers is very steady.",
                    likesCount = 31,
                    commentsCount = 7,
                    isVerifiedExpert = false,
                    isOfficialAdvisory = false
                ),
                CommunityPost(
                    id = "post_3",
                    authorName = "Anand Patil",
                    authorLocation = "Dharwad",
                    cropTag = "Paddy Management",
                    content = "Tested alternate wetting and drying (AWD) technique this season. Saved around 28% tube well power with zero yield penalty on BPT 5204.",
                    likesCount = 56,
                    commentsCount = 19,
                    isVerifiedExpert = false,
                    isOfficialAdvisory = false
                )
            )
            database.communityDao().insertPosts(posts)

            // Seed Expert Reviews
            val reviews = listOf(
                ExpertReview(
                    id = "rev_1",
                    farmId = "farm_c",
                    cropName = "Chilli",
                    question = "Leaf edges curling upwards with slight chlorosis on apical leaves.",
                    aiPreliminaryDiagnosis = "Probable Chilli Thrips infestation or early Leaf Curl Virus vector.",
                    aiConfidencePercent = 78,
                    status = "Reviewing",
                    expertName = "Dr. Radhika Sharma (Entomologist)",
                    expertResponse = "Inspecting microscope images. Recommend applying neem oil 10,000 ppm (3ml/L) as first line response before chemical sprays."
                )
            )
            database.expertDao().insertReviews(reviews)
        }
    }

    suspend fun updateProfile(profile: FarmerProfile) {
        database.farmerDao().insertOrUpdateProfile(profile)
    }

    suspend fun selectFarm(farmId: String) {
        database.farmDao().selectFarm(farmId)
    }

    suspend fun addFarm(farm: Farm, cycle: CropCycle) {
        database.farmDao().insertFarm(farm)
        database.cropDao().insertCropCycle(cycle)
    }

    suspend fun deleteFarm(farmId: String) {
        database.farmDao().deleteFarm(farmId)
    }

    suspend fun addChatMessage(message: ChatMessage) {
        database.chatDao().insertMessage(message)
    }

    suspend fun clearChat() {
        database.chatDao().clearChat()
    }

    suspend fun submitExpertReview(review: ExpertReview) {
        database.expertDao().insertReview(review)
    }

    suspend fun addMemory(memory: AIMemory) {
        database.memoryDao().insertMemory(memory)
    }

    suspend fun updateMemory(memory: AIMemory) {
        database.memoryDao().updateMemory(memory)
    }

    suspend fun deleteMemory(memoryId: String) {
        database.memoryDao().deleteMemory(memoryId)
    }

    suspend fun markAlertRead(alertId: String) {
        database.alertDao().markAsRead(alertId)
    }
}
