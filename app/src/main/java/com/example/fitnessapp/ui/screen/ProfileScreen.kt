package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.R
import com.android.volley.RequestQueue
import com.example.fitnessapp.network.NetworkManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import coil3.compose.rememberAsyncImagePainter

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
    var isLoading by remember { mutableStateOf(true) }
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val defaultPic = painterResource(id = R.drawable.default_profile)

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            profilePicUri = uri
            NetworkManager.uploadProfileImage(userId, uri, context) { success, imageUrl ->
                if (success && !imageUrl.isNullOrEmpty()) {
                    profileImageUrl = imageUrl
                    message = "Profile image updated."
                } else {
                    message = imageUrl ?: "Image upload failed."
                }
            }
        }
    }

    // Fetch profile info on first composition
    LaunchedEffect(userId) {
        isLoading = true
        NetworkManager.readProfile(userId) { success, n, g, a, w, h, imgUrl ->
            if (success) {
                name = n
                gender = g
                age = a
                weight = w
                height = h
                profileImageUrl = imgUrl
            } else {
                message = n // n contains error message
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile", color = Color.Black) },
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
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile picture
            Box(modifier = Modifier.size(120.dp)) {
                val painter = when {
                    profilePicUri != null -> rememberAsyncImagePainter(profilePicUri)
                    !profileImageUrl.isNullOrEmpty() -> rememberAsyncImagePainter(profileImageUrl)
                    else -> defaultPic
                }
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.7f))
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Change Photo", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Text Fields for student information
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            // Gender selection with checkboxes
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gender:", modifier = Modifier.padding(end = 8.dp))
                val isMale = gender == "Male"
                val isFemale = gender == "Female"
                Checkbox(
                    checked = isMale,
                    onCheckedChange = { checked ->
                        if (checked) gender = "Male" else if (isFemale) gender = ""
                    }
                )
                Text("Male", modifier = Modifier.padding(end = 16.dp))
                Checkbox(
                    checked = isFemale,
                    onCheckedChange = { checked ->
                        if (checked) gender = "Female" else if (isMale) gender = ""
                    }
                )
                Text("Female")
            }
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Your Age (Years)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Your Weight (Kg)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            TextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(" Your Height (CM)") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isSubmitting = true
                        NetworkManager.updateProfile(
                            userId = userid,
                            name = name,
                            gender = gender,
                            age = age.toIntOrNull() ?: 0,
                            weight = weight.toFloatOrNull() ?: 0f,
                            height = height.toFloatOrNull() ?: 0f
                        ) { success, msg ->
                            message = msg
                            isSubmitting = false
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    enabled = !isSubmitting && !isLoading
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 3.dp)
                    } else {
                        Text("Update Profile")
                    }

                }
            }
            // Display result message
            Text(message, modifier = Modifier.padding(8.dp))
        }
    }
}

fun onImagePicked(uri: Uri?) {
    // This function should be implemented in the composable scope, not as a top-level function.
    // For Compose, move this logic inside the composable and call NetworkManager.uploadProfileImage as shown below:
    //
    // if (uri != null) {
    //     profilePicUri = uri
    //     NetworkManager.uploadProfileImage(userId, uri, context) { success, imageUrl ->
    //         if (success && !imageUrl.isNullOrEmpty()) {
    //             profileImageUrl = imageUrl
    //             message = "Profile image updated."
    //         } else {
    //             message = imageUrl ?: "Image upload failed."
    //         }
    //     }
    // }
    //
    // Remove this top-level function if not used elsewhere.
}
