package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran")
data class QuranAyat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val suraId: Int,
    val verseId: Int,
    val arabicText: String,
    val indoText: String,
    val latinText: String
)
