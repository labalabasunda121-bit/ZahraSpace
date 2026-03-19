package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "quran_id")
data class QuranAyat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val suraId: Int,
    val verseID: Int,
    val ayahText: String,
    val indoText: String,
    val readText: String
)
