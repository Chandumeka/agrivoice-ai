package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmer_profile WHERE id = 'primary_farmer' LIMIT 1")
    fun getProfile(): Flow<FarmerProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: FarmerProfile)
}

@Dao
interface FarmDao {
    @Query("SELECT * FROM farms ORDER BY createdAt ASC")
    fun getAllFarms(): Flow<List<Farm>>

    @Query("SELECT * FROM farms WHERE isSelected = 1 LIMIT 1")
    fun getSelectedFarm(): Flow<Farm?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: Farm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarms(farms: List<Farm>)

    @Update
    suspend fun updateFarm(farm: Farm)

    @Query("UPDATE farms SET isSelected = (CASE WHEN id = :farmId THEN 1 ELSE 0 END)")
    suspend fun selectFarm(farmId: String)

    @Query("DELETE FROM farms WHERE id = :farmId")
    suspend fun deleteFarm(farmId: String)
}

@Dao
interface CropDao {
    @Query("SELECT * FROM crop_cycles WHERE farmId = :farmId LIMIT 1")
    fun getCropCycle(farmId: String): Flow<CropCycle?>

    @Query("SELECT * FROM crop_cycles")
    fun getAllCropCycles(): Flow<List<CropCycle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropCycle(cycle: CropCycle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropCycles(cycles: List<CropCycle>)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM farm_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<FarmAlert>>

    @Query("SELECT * FROM farm_alerts WHERE farmId = :farmId OR farmId = 'ALL' ORDER BY timestamp DESC")
    fun getAlertsForFarm(farmId: String): Flow<List<FarmAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: FarmAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<FarmAlert>)

    @Query("UPDATE farm_alerts SET isRead = 1 WHERE id = :alertId")
    suspend fun markAsRead(alertId: String)

    @Query("DELETE FROM farm_alerts WHERE id = :alertId")
    suspend fun deleteAlert(alertId: String)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessage>)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}

@Dao
interface SoilDao {
    @Query("SELECT * FROM soil_reports WHERE farmId = :farmId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestSoilReport(farmId: String): Flow<SoilReport?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilReport(report: SoilReport)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilReports(reports: List<SoilReport>)
}

@Dao
interface MarketDao {
    @Query("SELECT * FROM market_prices ORDER BY modalPriceQuintal DESC")
    fun getAllMarketPrices(): Flow<List<MarketPrice>>

    @Query("SELECT * FROM market_prices WHERE cropName = :cropName ORDER BY modalPriceQuintal DESC")
    fun getPricesForCrop(cropName: String): Flow<List<MarketPrice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketPrices(prices: List<MarketPrice>)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM ai_memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<AIMemory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: AIMemory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemories(memories: List<AIMemory>)

    @Update
    suspend fun updateMemory(memory: AIMemory)

    @Query("DELETE FROM ai_memories WHERE id = :memoryId")
    suspend fun deleteMemory(memoryId: String)
}

@Dao
interface ExpertDao {
    @Query("SELECT * FROM expert_reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ExpertReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ExpertReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ExpertReview>)

    @Update
    suspend fun updateReview(review: ExpertReview)
}

@Dao
interface CommunityDao {
    @Query("SELECT * FROM community_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<CommunityPost>)
}

@Database(
    entities = [
        FarmerProfile::class,
        Farm::class,
        CropCycle::class,
        FarmAlert::class,
        ChatMessage::class,
        SoilReport::class,
        MarketPrice::class,
        AIMemory::class,
        ExpertReview::class,
        CommunityPost::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AgriVoiceDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun farmDao(): FarmDao
    abstract fun cropDao(): CropDao
    abstract fun alertDao(): AlertDao
    abstract fun chatDao(): ChatDao
    abstract fun soilDao(): SoilDao
    abstract fun marketDao(): MarketDao
    abstract fun memoryDao(): MemoryDao
    abstract fun expertDao(): ExpertDao
    abstract fun communityDao(): CommunityDao
}
