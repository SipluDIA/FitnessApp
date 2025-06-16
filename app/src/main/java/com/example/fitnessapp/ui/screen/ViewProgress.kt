package com.example.fitnessapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ViewProgress(navController: NavHostController, userId: Int) {
    var isLoading by remember { mutableStateOf(true) }
    var activities by remember { mutableStateOf<Map<String, List<NetworkManager.ActivityItem>>>(emptyMap()) }
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
        LazyColumn {
            activities.forEach { (type, list) ->
                item {
                    Text(type, style = MaterialTheme.typography.titleMedium)
                    Divider()
                }
                items(list) { activity ->
                    val start = runCatching { LocalDateTime.parse(activity.startTime, formatter) }.getOrNull()
                    val end = runCatching { LocalDateTime.parse(activity.endTime, formatter) }.getOrNull()
                    val duration = if (start != null && end != null) {
                        val d = Duration.between(start, end)
                        String.format("%02d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart())
                    } else "-"
                    Card(modifier = Modifier.padding(8.dp)) {
                        Text("Date: ${activity.startTime}")
                        Text("Step Count: ${activity.stepCount}")
                        val caloricBurn = activity.stepCount * 0.04
                        Text("Calories Burn: %.2f".format(caloricBurn))
                        Text("Time: $duration")
                        Text("Start: ${activity.startTime}")
                        Text("End: ${activity.endTime}")
                    }
                }
            }
        }
    }
}