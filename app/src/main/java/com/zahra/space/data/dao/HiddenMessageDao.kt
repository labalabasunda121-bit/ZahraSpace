package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.HiddenMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface HiddenMessageDao {
    @Query("SELECT * FROM hidden_messages WHERE isFound = 0 ORDER BY RANDOM() LIMIT 1")
    fun getRandomHiddenMessage(): Flow<HiddenMessage?>
    
    @Query("SELECT * FROM hidden_messages WHERE location = :location AND isFound = 0 ORDER BY RANDOM() LIMIT 1")
    fun getMessageByLocation(location: String): Flow<HiddenMessage?>
    
    @Query("SELECT COUNT(*) FROM hidden_messages WHERE isFound = 1")
    fun getFoundCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM hidden_messages")
    suspend fun getTotalCount(): Int
    
    @Insert
    suspend fun insert(message: HiddenMessage)
    
    @Insert
    suspend fun insertAll(messages: List<HiddenMessage>)
    
    @Update
    suspend fun update(message: HiddenMessage)
}
