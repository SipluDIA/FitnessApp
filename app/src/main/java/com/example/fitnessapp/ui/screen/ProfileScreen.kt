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

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.GradientEnd
import com.example.fitnessapp.ui.theme.GradientStart

import com.example.fitnessapp.ui.theme.Grey3
import com.example.fitnessapp.ui.theme.Grey4
import com.example.fitnessapp.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUpdate(userId: Int, navController: NavHostController) {
    var name by remember { mutableStateOf("") }
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
                title = { Text("Complete Profile", color = Color.Black) },
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
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_complete_profile),
                contentDescription = "Complete profile image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.height(200.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Hello, ${name}",
                color = Black,
                fontSize = 20.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Let’s complete your profile",
                color = Black,
                fontSize = 12.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Gender selection with checkboxes
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color=Grey4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(R.drawable.ic_leading_full_name),
                    contentDescription = null
                )
                Text("Gender:", modifier = Modifier.padding(start = 8.dp, end = 8.dp))
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

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                singleLine = true,
                value = age,
                onValueChange = { age = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = {
                    Text(
                        text = "Your Age (Years)",
                        color = Grey3,
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_leading_full_name),
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    singleLine = true,
                    value = weight,
                    onValueChange = { weight = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(
                            text = "Your Weight",
                            color = Grey3,
                            fontSize = 14.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.ic_leading_weight),
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_button_kg),
                    contentDescription = "weight",
                    modifier = Modifier
                        .height(50.dp)
                )

            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    singleLine = true,
                    value = height,
                    onValueChange = { height = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(
                            text = "Your Height",
                            color = Grey3,
                            fontSize = 14.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.ic_leading_height),
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_button_cm),
                    contentDescription = "weight",
                    modifier = Modifier
                        .height(50.dp)
                )

            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, top = 15.dp, bottom = 10.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)),
                            shape = ButtonDefaults.shape
                        )
                        .height(55.dp),
                    onClick = {
                        isSubmitting = true
                        NetworkManager.updateProfile(
                            userId = userId,
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

                    enabled = !isSubmitting && !isLoading

                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 3.dp)
                    } else {
                        Text(
                            "Update Profile", color = Color.White, fontSize = 16.sp,
                            fontFamily = poppinsFamily, fontWeight = FontWeight.Bold
                        )
                    }

                }

            }
            // Display result message
            Text(message, modifier = Modifier.padding(8.dp))
        }
    }
}

