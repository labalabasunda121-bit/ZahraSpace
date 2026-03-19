package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "daily_checklist")
data class DailyChecklist(
    @PrimaryKey val date: String,
    val sholatSubuh: Boolean = false,
    val sholatDzuhur: Boolean = false,
    val sholatAshar: Boolean = false,
    val sholatMaghrib: Boolean = false,
    val sholatIsya: Boolean = false,
    val sholatDhuha: Boolean = false,
    val dzikirPagi: Boolean = false,
    val dzikirPetang: Boolean = false,
    val bacaQuran: Boolean = false,
    val totalPoints: Int = 0,
    val imanChange: Int = 0
)
