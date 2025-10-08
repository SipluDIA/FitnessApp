package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import com.example.fitnessapp.network.NetworkManager.ActivityItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySummery(userId: Int, navController: NavHostController) {
    val context = LocalContext.current
    val loading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf("") }
    val activity = remember { mutableStateOf<ActivityItem?>(null) }

    LaunchedEffect(userId) {
        NetworkManager.getLatestActivity(userId) { success, result, message ->
            loading.value = false
            if (success) {
                activity.value = result
            } else {
                error.value = message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Summary", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {  // Add a back button
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                loading.value -> Text("Loading summary...")
                error.value.isNotEmpty() -> Text("Error: ${error.value}")
                activity.value != null -> {
                    val item = activity.value!!
                    // ActivityItem does not have activityType, so display generic or infer from context
                    Text("Steps: ${item.stepCount}")
                    Text("Start: ${item.startTime}")
                    Text("End: ${item.endTime}")
                }
                else -> Text("No activity found.")
            }
        }
    }
}
