package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.volley.RequestQueue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUpdate(navController: NavHostController, userId: Int, userName: String) {
    var userid by remember { mutableIntStateOf(userId) }
    var name by remember { mutableStateOf(userName) }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) } //To disable button

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile", color = Color.White) },
                colors = androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray),
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
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text Fields for student information
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Your Age") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Your Weight") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(" Your Height") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            // Button to add student data
            Button(
                onClick = {

                },
                modifier = Modifier.padding(16.dp),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 3.dp)
                } else {
                    Text("Profile Updated")
                }

            }
            // Display result message
            Text(message, modifier = Modifier.padding(8.dp))
        }
    }
}
