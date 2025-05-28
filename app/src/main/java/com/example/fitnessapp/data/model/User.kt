package com.example.fitnessapp.data.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val age: Int?,
    val weightKg: Float?,
    val heightCm: Float?
)

