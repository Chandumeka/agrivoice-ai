package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_profile")
data class FarmerProfile(
    @PrimaryKey val id: String = "primary_farmer",
    val name: String = "Ramesh Kumar",
    val languageCode: String = "en", // "en", "hi", "kn", "te", "ta", "ml", "mr", "bn"
    val location: String = "Kolar, Karnataka",
    val phone: String = "+91 98765 43210",
    val experienceYears: Int = 12,
    val preferredUnits: String = "Acres / Quintal",
    val isDemoMode: Boolean = true,
    val hasCompletedOnboarding: Boolean = true,
    val voiceSpeed: Float = 1.0f,
    val autoVoiceResponse: Boolean = true,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "farms")
data class Farm(
    @PrimaryKey val id: String,
    val name: String,
    val sizeAcres: Double,
    val mainCrop: String,
    val soilType: String,
    val location: String,
    val healthScore: Int, // 0 - 100
    val healthStatus: String, // "Healthy", "Watch", "Attention"
    val irrigationType: String = "Drip Irrigation",
    val isSelected: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "crop_cycles")
data class CropCycle(
    @PrimaryKey val id: String,
    val farmId: String,
    val cropName: String,
    val variety: String,
    val currentStage: String, // "Planting", "Germination", "Vegetative Growth", "Flowering", "Fruit Development", "Harvest"
    val stageProgressPercent: Int,
    val daysSincePlanting: Int,
    val totalEstimatedDays: Int,
    val targetYieldQuintals: Double,
    val plantingDate: String,
    val harvestDate: String,
    val currentAdvisory: String
)

@Entity(tableName = "farm_alerts")
data class FarmAlert(
    @PrimaryKey val id: String,
    val farmId: String,
    val title: String,
    val message: String,
    val severity: String, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val category: String, // "WEATHER", "DISEASE", "IRRIGATION", "MARKET"
    val actionableStep: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val role: String, // "user", "assistant"
    val text: String,
    val specialistAgent: String = "Orchestrator", // "Crop", "Weather", "Disease", "Irrigation", "Soil", "Market", "Farm Planning", "Alert", "Knowledge"
    val languageCode: String = "en",
    val confidence: Float = 0.92f,
    val dataSource: String = "AgriVoice Verified Models & IMD Weather",
    val timestamp: Long = System.currentTimeMillis(),
    val isVoiceInput: Boolean = false
)

@Entity(tableName = "soil_reports")
data class SoilReport(
    @PrimaryKey val id: String,
    val farmId: String,
    val testDate: String,
    val ph: Double,
    val nitrogenKgPerHa: Double,
    val phosphorusKgPerHa: Double,
    val potassiumKgPerHa: Double,
    val organicCarbonPercent: Double,
    val electricalConductivity: Double,
    val healthSummary: String,
    val recommendedActions: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "market_prices")
data class MarketPrice(
    @PrimaryKey val id: String,
    val cropName: String,
    val mandiName: String,
    val district: String,
    val state: String,
    val modalPriceQuintal: Double,
    val minPriceQuintal: Double,
    val maxPriceQuintal: Double,
    val priceTrend: String, // "UP", "DOWN", "STABLE"
    val priceChangePercent: Double,
    val distanceKm: Int,
    val estimatedTransportCostQuintal: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "ai_memories")
data class AIMemory(
    @PrimaryKey val id: String,
    val category: String,
    val fact: String,
    val isEnabled: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "expert_reviews")
data class ExpertReview(
    @PrimaryKey val id: String,
    val farmId: String,
    val cropName: String,
    val question: String,
    val aiPreliminaryDiagnosis: String,
    val aiConfidencePercent: Int,
    val status: String, // "Submitted", "Assigned", "Reviewing", "Answered"
    val expertName: String = "Pending Agricultural Scientist",
    val expertResponse: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorLocation: String,
    val cropTag: String,
    val content: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isVerifiedExpert: Boolean = false,
    val isOfficialAdvisory: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
