package com.zahra.space.utils
import android.content.Context
import com.zahra.space.data.entity.Hadist
import com.zahra.space.data.dao.HadistDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
class HaditsParser(private val context: Context, private val hadistDao: HadistDao) {
    suspend fun parseAllHadits() = withContext(Dispatchers.IO) {
        parseFile("maliks_muwataa_ahadith_mushakkala_mufassala.utf8.csv", "Malik's Muwata")
        parseFile("musnad_ahmad_ibn-hanbal_ahadith_mushakkala.utf8.csv", "Musnad Ahmad")
    }
    private suspend fun parseFile(filename: String, book: String) {
        try {
            context.assets.open("data/hadits/$filename").use { input ->
                val reader = BufferedReader(InputStreamReader(input))
                val batch = mutableListOf<Hadist>()
                reader.useLines { lines ->
                    lines.forEachIndexed { index, line ->
                        if (index == 0) return@forEachIndexed
                        val parts = line.split(",")
                        if (parts.size >= 5) {
                            batch.add(Hadist(
                                arabicText = parts.getOrElse(1) { "" },
                                translation = parts.getOrElse(2) { "" },
                                narrator = parts.getOrElse(3) { "" },
                                book = book,
                                grade = parts.getOrElse(4) { "" }
                            ))
                        }
                        if (batch.size >= 100) {
                            hadistDao.insertAll(batch.toList())
                            batch.clear()
                        }
                    }
                }
                if (batch.isNotEmpty()) hadistDao.insertAll(batch)
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
}
