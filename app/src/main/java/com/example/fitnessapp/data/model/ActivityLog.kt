package com.example.fitnessapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val activityType: String,
    val activityDate: String,
    val durationMinutes: Int?,
    val distanceKm: Float?,
    val exerciseName: String? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weightKg: Float? = null,
    val notes: String? = null
)