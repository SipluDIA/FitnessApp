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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.util.Log
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
fun DashboardScreen(userId: Int, navController: NavHostController, profilePicUri: Uri? = null) {
    var userName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    val defaultPic = painterResource(id = R.drawable.default_profile)

    // Fetch user info on first composition
    LaunchedEffect(userId) {
        isLoading = true
        com.example.fitnessapp.network.NetworkManager.readProfile(userId) { success, n, _, _, _, _, imgUrl ->
            if (success) {
                userName = n
                profileImageUrl = imgUrl
            } else {
                userName = "Unknown"
            }
            isLoading = false
        }
    }

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
                .padding(paddingValues)
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile picture before welcome
            Image(
                painter = when {
                    profilePicUri != null -> rememberAsyncImagePainter(profilePicUri)
                    profileImageUrl != null && profileImageUrl!!.isNotBlank() -> rememberAsyncImagePainter(profileImageUrl)
                    else -> defaultPic
                },
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
                onClick = { navController.navigate("profile/$userId") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Your Profile")
            }
            Button(
                onClick = { navController.navigate("goal/$userId") }, // Navigate to All
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Set Your Goal")
            }
            Button( // New button for viewing students by course.
                onClick = { navController.navigate("activity/$userId") },  // Navigate with a default course
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Record Activity")
            }
            Button( // New button for viewing students by course.
                onClick = { navController.navigate("progress/$userId") },  // Navigate with a default course
                modifier = Modifier.padding(8.dp)
            ) {
                Text("View Progress")
            }
        }
    }
}