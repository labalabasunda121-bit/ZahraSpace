package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "dzikir")
data class Dzikir(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val arabicText: String,
    val translation: String,
    val latinText: String,
    val count: Int,
    val category: String
)
