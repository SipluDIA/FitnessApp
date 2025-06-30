package com.example.fitnessapp.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_activities")
data class OfflineActivity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val activityType: String,
    val stepCount: Int,
    val startTime: String,
    val endTime: String,
    val synced: Boolean = false
)
