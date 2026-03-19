package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "1",
    val name: String = "",
    val birthDate: Long = 0,
    val avatar: String = "👩",
    val totalPoints: Long = 0,
    val streak: Int = 0,
    val imanLevel: Int = 50,
    val installDate: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis(),
    val hasCompletedOnboarding: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val lastHaidDate: Long? = null,
    val haidCycle: Int = 28
)
