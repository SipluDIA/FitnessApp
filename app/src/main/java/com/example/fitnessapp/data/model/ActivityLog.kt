package com.example.fitnessapp.data.model

data class ActivityLog(
    val id: String, // Or Int
    val userId: Int,
    val activityType: String,
    val activityDate: String, // Use Instant or LocalDateTime for better handling
    val durationMinutes: Int?,
    val distanceKm: Float?,
    val exerciseName: String? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weightKg: Float? = null,
    val notes: String? = null
)