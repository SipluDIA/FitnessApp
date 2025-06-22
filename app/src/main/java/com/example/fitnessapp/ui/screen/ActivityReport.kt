package com.example.fitnessapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityReport(navController: NavHostController, userId: Int) {
    var isLoading by remember { mutableStateOf(true) }
    var todayList by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var monthMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        isLoading = true
        NetworkManager.getWalkingActivityReport(userId) { success, today, month, msg ->
            if (success) {
                todayList = today
                monthMap = month
                error = null
            } else {
                error = msg
            }
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Walking Activity Report", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            // Column Chart for Today
            Text("Today's Steps by Time", style = MaterialTheme.typography.titleMedium)
            if (todayList.isEmpty()) {
                Text("No data for today.")
            } else {
                SimpleColumnChart(
                    data = todayList.map { it.second },
                    labels = todayList.map { it.first },
                    chartHeight = 220.dp,
                    barColor = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Column Chart for Month
            Text("This Month's Steps by Date", style = MaterialTheme.typography.titleMedium)
            if (monthMap.isEmpty()) {
                Text("No data for this month.")
            } else {
                val monthEntries = monthMap.entries.toList()
                SimpleColumnChart(
                    data = monthEntries.map { it.value },
                    labels = monthEntries.map { it.key },
                    chartHeight = 220.dp,
                    barColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun SimpleColumnChart(
    data: List<Int>,
    labels: List<String>,
    chartHeight: Dp,
    barColor: Color
) {
    val maxVal = (data.maxOrNull() ?: 1).toFloat()
    val barWidth = if (data.size > 0) (1f / data.size) else 0.1f
    val labelCount = if (labels.size > 4) 4 else labels.size
    val labelStep = if (labels.size > 4) (labels.size / labelCount) else 1

    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier
            .height(chartHeight)
            .fillMaxWidth()
        ) {
            val width = size.width
            val height = size.height
            val barSpace = width / (data.size * 1.5f)
            val barActualWidth = barSpace
            data.forEachIndexed { idx, value ->
                val left = idx * barSpace * 1.5f
                val barHeight = if (maxVal == 0f) 0f else (value / maxVal) * height * 0.9f
                drawRect(
                    color = barColor,
                    topLeft = Offset(left, height - barHeight),
                    size = androidx.compose.ui.geometry.Size(barActualWidth, barHeight)
                )
            }
            // Draw X axis
            drawLine(
                color = Color.Gray,
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 2f
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEachIndexed { idx, label ->
                if (idx % labelStep == 0 || labels.size <= 4) {
                    Text(label, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
            }
        }
    }
}
