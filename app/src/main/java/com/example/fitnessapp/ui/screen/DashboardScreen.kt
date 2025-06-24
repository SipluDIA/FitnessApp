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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.fitnessapp.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.GradientEnd
import com.example.fitnessapp.ui.theme.GradientStart
import com.example.fitnessapp.ui.theme.Grey1
import com.example.fitnessapp.ui.theme.Grey2
import com.example.fitnessapp.ui.theme.Grey3
import com.example.fitnessapp.ui.theme.Pink1
import com.example.fitnessapp.ui.theme.PurpleLight
import com.example.fitnessapp.ui.theme.poppinsFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(userId: Int, navController: NavHostController, profilePicUri: Uri? = null) {
    var userName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var weight by remember { mutableStateOf<Float?>(null) }
    var height by remember { mutableStateOf<Float?>(null) }
    val defaultPic = painterResource(id = R.drawable.default_profile)

    // Fetch user info on first composition
    LaunchedEffect(userId) {
        isLoading = true
        com.example.fitnessapp.network.NetworkManager.readProfile(userId) { success, n, _, _, w, h, imgUrl ->
            if (success) {
                userName = n
                profileImageUrl = imgUrl
                weight = w.toFloatOrNull()
                height = h.toFloatOrNull()
            } else {
                userName = "Unknown"
            }
            isLoading = false
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("activity/$userId") },
                containerColor = Color(0xFFFF9800),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 30.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "activity")
            }
        },
        bottomBar = {
            BottomAppBar(actions = {
                IconButton(onClick = { navController.navigate("profile/$userId") }) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile")

                }
                IconButton(onClick = { navController.navigate("goal/$userId") }) {
                    Icon(Icons.Filled.AddCircle, contentDescription = "Goals")
                }
                IconButton(onClick = { navController.navigate("progress/$userId") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Progress")
                }
                IconButton(onClick = { navController.navigate("report/$userId") }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Report")
                }
            })
        }

    ) { paddingValues ->
        LazyColumn(content = {
            item {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 25.dp)
                        .wrapContentSize(),

                    ) {
                    // Profile picture before welcome
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                "Welcome Back,",
                                color = Grey1,
                                fontSize = 12.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                "$userName!",
                                color = Black,
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Bold
                            )

                        }
                        Box() {
                            Image(
                                painter = painterResource(id = R.drawable.ic_icon_notification),
                                contentDescription = null,
                                modifier = Modifier
                                    .background(color = Grey2, shape = RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                                    .clickable { println("Button Clicked!") })
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bg_home_header),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.matchParentSize(),
                            alignment = Alignment.Center

                        )

                        Column(

                            modifier = Modifier
                                .height(150.dp)
                                .padding(start = 18.dp, top = 22.dp, bottom = 12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "BMI (Body Mass Index)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                            // BMI Calculation and Category
                            val bmi = if (weight != null && height != null && height!! > 0f) {
                                val hM = height!! / 100f
                                weight!! / (hM * hM)
                            } else null
                            val bmiCategory = when {
                                bmi == null -> "-"
                                bmi < 18.5 -> "Underweight"
                                bmi < 25 -> "Normal Weight"
                                bmi < 30 -> "Overweight"
                                else -> "Obesity"
                            }
                            if (bmi != null) {
                                Text(
                                    "Your BMI: %.1f".format(bmi),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "You are: $bmiCategory",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Text(
                                    "Your BMI: -",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "You are: -",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Button(

                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(start = 0.dp, end = 0.dp, bottom = 0.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .height(30.dp),
                                onClick = { navController.navigate("profile/$userId")},
                                contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text(
                                    "View Profile", color = Pink1, fontSize = 10.sp,
                                    fontFamily = poppinsFamily, fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = PurpleLight, shape = RoundedCornerShape(12.dp))
                            .padding(15.dp)
                    ) {
                        Text(
                            "Your Goal", color = Black, fontSize = 14.sp,
                            fontFamily = poppinsFamily, fontWeight = FontWeight.Medium
                        )
                        Button(

                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 0.dp, end = 0.dp, bottom = 0.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            GradientStart,
                                            GradientEnd
                                        )
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .height(30.dp),
                            onClick = { navController.navigate("goal/$userId") },
                            contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                "Check", color = Color.White, fontSize = 12.sp,
                                fontFamily = poppinsFamily, fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        "Activity Status",
                        color = Black,
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.Top, modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(color = PurpleLight, shape = RoundedCornerShape(12.dp))
                            .padding(15.dp)
                    ) {
                        Text(
                            "Walking",
                            color = Black,
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            "78 BPM",
                            fontSize = 14.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(
                                brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd))
                            )
                        )
                        Text(
                            "Running",
                            color = Black,
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            "78 BPM",
                            fontSize = 14.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(
                                brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd))
                            )
                        )
                        Text(
                            "Cycling",
                            color = Black,
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            "78 BPM",
                            fontSize = 14.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(
                                brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd))
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(modifier = Modifier.fillMaxSize()) {
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            onClick = {navController.navigate("progress/$userId")},
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier

                                    .padding(10.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(
                                        start = 12.dp,
                                        top = 12.dp,
                                        end = 12.dp
                                    )
                                ) {
                                    Text(
                                        "Walking",
                                        color = Black,
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        "View Activity",
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        style = TextStyle(
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    GradientStart,
                                                    GradientEnd
                                                )
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            onClick = {navController.navigate("report/$userId")},
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(
                                        start = 12.dp,
                                        top = 12.dp,
                                        end = 12.dp
                                    )
                                ) {
                                    Text(
                                        "Running",
                                        color = Black,
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        "View Progress",
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        style = TextStyle(
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    GradientStart,
                                                    GradientEnd
                                                )
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }, modifier = Modifier.fillMaxSize())
    }
}

/*

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
*/