package com.example.fitnessapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val age: Int?,
    val weightKg: Float?,
    val heightCm: Float?
)

