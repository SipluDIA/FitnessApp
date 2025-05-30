package com.example.fitnessapp.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.fitnessapp.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(userId: Int, userName: String, navController: NavHostController, profilePicUri: Uri? = null) {
    val defaultPic = painterResource(id = R.drawable.default_profile)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fitness App", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
            // Profile picture before welcome
            Image(
                painter = if (profilePicUri != null) rememberAsyncImagePainter(profilePicUri) else defaultPic,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
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