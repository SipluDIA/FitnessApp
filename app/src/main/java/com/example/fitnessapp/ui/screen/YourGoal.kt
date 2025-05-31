package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourGoal(navController: NavHostController, userId: Int) {
    var walking by remember { mutableStateOf("") }
    var running by remember { mutableStateOf("") }
    var cycling by remember { mutableStateOf("") }
    var swimming by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        isLoading = true
        NetworkManager.getGoal(userId) { success, w, r, c, s ->
            if (success) {
                walking = w.toString()
                running = r.toString()
                cycling = c.toString()
                swimming = s.toString()
            }
            isLoading = false
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Goals", color = Color.Black) },
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
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Set Your Monthly Goals", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = walking,
                onValueChange = { walking = it.filter { ch -> ch.isDigit() } },
                label = { Text("Walking (steps)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            OutlinedTextField(
                value = running,
                onValueChange = { running = it.filter { ch -> ch.isDigit() } },
                label = { Text("Running (steps)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            OutlinedTextField(
                value = cycling,
                onValueChange = { cycling = it.filter { ch -> ch.isDigit() } },
                label = { Text("Cycling (meters)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            OutlinedTextField(
                value = swimming,
                onValueChange = { swimming = it.filter { ch -> ch.isDigit() } },
                label = { Text("Swimming (meters)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isSubmitting = true
                        NetworkManager.setOrUpdateGoal(
                            userId = userId,
                            walking = walking.toIntOrNull() ?: 0,
                            running = running.toIntOrNull() ?: 0,
                            cycling = cycling.toIntOrNull() ?: 0,
                            swimming = swimming.toIntOrNull() ?: 0
                        ) { success, msg ->
                            message = msg
                            isSubmitting = false
                        }
                    },
                    enabled = !isSubmitting,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    if (isSubmitting)
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 3.dp
                        )
                    else Text("Save Goal")
                }
            }
            Text(message, modifier = Modifier.padding(8.dp))
        }
    }
}