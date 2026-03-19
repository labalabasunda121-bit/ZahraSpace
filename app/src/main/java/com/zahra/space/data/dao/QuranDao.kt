package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.QuranAyat
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {
    @Query("SELECT * FROM quran WHERE suraId = :surahId ORDER BY verseId")
    fun getAyatBySurah(surahId: Int): Flow<List<QuranAyat>>
    
    @Query("SELECT * FROM quran WHERE suraId = :surahId AND verseId = :verseId")
    fun getAyat(surahId: Int, verseId: Int): Flow<QuranAyat?>
    
    @Query("SELECT DISTINCT suraId, MIN(id) as id, '' as arabicText, '' as indoText, '' as latinText, 0 as verseId FROM quran GROUP BY suraId ORDER BY suraId")
    fun getSurahList(): Flow<List<QuranAyat>>
}
