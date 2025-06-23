package com.example.fitnessapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ViewProgress(navController: NavHostController, userId: Int) {
    var isLoading by remember { mutableStateOf(true) }
    var activities by remember {
        mutableStateOf<Map<String, List<NetworkManager.ActivityItem>>>(
            emptyMap()
        )
    }
    var error by remember { mutableStateOf<String?>(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    LaunchedEffect(userId) {
        isLoading = true
        NetworkManager.getActivities(userId) { success, data, msg ->
            if (success) {
                activities = data
                error = null
            } else {
                error = msg
            }
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (error != null) {
        Text("Error: $error", color = MaterialTheme.colorScheme.error)
    } else {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("View Activity", color = Color.Black) },
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
                    .padding(paddingValues)
                    .wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                LazyColumn {
                    activities.forEach { (type, list) ->
                        item {
                            Text(type, style = MaterialTheme.typography.titleMedium)
                            Divider()
                        }
                        items(list) { activity ->
                            val start = runCatching {
                                LocalDateTime.parse(
                                    activity.startTime,
                                    formatter
                                )
                            }.getOrNull()
                            val end =
                                runCatching {
                                    LocalDateTime.parse(
                                        activity.endTime,
                                        formatter
                                    )
                                }.getOrNull()
                            val duration = if (start != null && end != null) {
                                val d = Duration.between(start, end)
                                String.format(
                                    "%02d:%02d:%02d",
                                    d.toHours(),
                                    d.toMinutesPart(),
                                    d.toSecondsPart()
                                )
                            } else "-"
                            Card(
                                modifier = Modifier.padding(10.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Text("Date: ${activity.startTime}",fontWeight = FontWeight.W700)
                                Text("Step Count: ${activity.stepCount}")
                                val caloricBurn = activity.stepCount * 0.04
                                Text("Calories Burn: %.2f".format(caloricBurn),fontWeight = FontWeight.W700,color= Color.Red)
                                Text("Time: $duration",color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

