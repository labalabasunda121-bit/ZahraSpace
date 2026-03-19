package com.zahra.space.data.dao
import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.QuranAyat
import kotlinx.coroutines.flow.Flow
@Dao
interface QuranDao {
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId ORDER BY verseID") fun getAyatBySurah(surahId: Int): Flow<List<QuranAyat>>
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId AND verseID = :verseId") fun getAyat(surahId: Int, verseId: Int): Flow<QuranAyat?>
    @Query("SELECT DISTINCT suraId, MIN(id) as id, '' as ayahText, '' as indoText, '' as readText, 0 as verseID FROM quran_id GROUP BY suraId ORDER BY suraId") fun getSurahList(): Flow<List<QuranAyat>>
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId AND verseID = :verseId") suspend fun getAyatSync(surahId: Int, verseId: Int): QuranAyat?
}
