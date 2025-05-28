package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
/*
@Composable
fun DashboardScreen(userId: Int, userName: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, $userName!", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your User ID: $userId", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        // Add your dashboard content here
    }
}

*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen( userId: Int, userName: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Student Management App", color = Color.White) },
                colors = androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use paddingValues from Scaffold
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
            verticalArrangement = Arrangement.Center
        ) {

            Text("Welcome, $userName!", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your User ID: $userId", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("profile/$userId/$userName") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Your Profile")
            }
            Button(
                onClick = { navController.navigate("profile/$userId/$userName") }, // Navigate to All
                modifier = Modifier.padding(8.dp)
            ) {
                Text("View All Students")
            }
            Button( // New button for viewing students by course.
                onClick = { navController.navigate("profile/$userId/$userName") },  // Navigate with a default course
                modifier = Modifier.padding(8.dp)
            ) {
                Text("View Students in Your Course")
            }
        }
    }
}