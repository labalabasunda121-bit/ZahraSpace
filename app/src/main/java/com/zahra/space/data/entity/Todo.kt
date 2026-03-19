package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String,
    val targetAmount: Long? = null,
    val targetUnit: String? = null,
    val currentAmount: Long = 0,
    val durationDays: Int,
    val startDate: Long,
    val endDate: Long,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
