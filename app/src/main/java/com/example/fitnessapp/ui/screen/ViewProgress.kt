package com.example.fitnessapp.ui.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ViewProgress(navController: NavHostController, userId: Int){
    Text("Your User ID: $userId", style = MaterialTheme.typography.bodyLarge)
}