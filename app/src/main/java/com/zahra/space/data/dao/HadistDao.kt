package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zahra.space.data.entity.Hadist
import kotlinx.coroutines.flow.Flow

@Dao
interface HadistDao {
    @Query("SELECT * FROM hadist WHERE book = :book LIMIT 50")
    fun getHadistByBook(book: String): Flow<List<Hadist>>
    
    @Query("SELECT * FROM hadist WHERE id = :id")
    fun getHadist(id: Long): Flow<Hadist?>
    
    @Insert
    suspend fun insertAll(hadist: List<Hadist>)
}
