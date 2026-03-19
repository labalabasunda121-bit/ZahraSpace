package com.zahra.space.utils

import android.content.Context
import com.zahra.space.data.entity.Hadist
import com.zahra.space.data.dao.HadistDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class HaditsParser(
    private val context: Context,
    private val hadistDao: HadistDao
) {
    
    suspend fun parseAllHadits() {
        withContext(Dispatchers.IO) {
            // Implementasi parsing CSV
        }
    }
}
