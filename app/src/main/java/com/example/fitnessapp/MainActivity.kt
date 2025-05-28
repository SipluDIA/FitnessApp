package com.example.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fitnessapp.network.NetworkManager
import com.example.fitnessapp.navigation.FitnessApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.init(applicationContext)
        setContent {
            FitnessApp()
        }
    }
}




