package com.example.fitnessapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.GradientEnd
import com.example.fitnessapp.ui.theme.GradientStart
import com.example.fitnessapp.ui.theme.Grey1
import com.example.fitnessapp.ui.theme.Grey3
import com.example.fitnessapp.ui.theme.poppinsFamily

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
                    title = {  Text(
                        "Activity Tracker",
                        color = Black,
                        fontSize = 20.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                    ) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 25.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                val cardShape = RoundedCornerShape(16.dp)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    activities.forEach { (type, list) ->
                        item {
                            Text(
                                type,
                                color = Black,
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
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
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier
                                    .shadow(
                                        shape = cardShape,
                                        spotColor = Grey3,
                                        elevation = 20.dp
                                    ),
                                shape = cardShape
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier

                                        .padding(18.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_profile_pic),
                                        contentDescription = null,
                                        modifier = Modifier.size(55.dp)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            "Date: ${activity.startTime}",
                                            color = Black,
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "Step Count: ${activity.stepCount}",
                                            color = GradientStart,
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Normal,
                                            maxLines = 1
                                        )
                                        val caloricBurn = activity.stepCount * 0.04
                                        Text(
                                            "Calories Burn: %.2f".format(caloricBurn),
                                            fontSize = 14.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Medium,
                                            style = TextStyle(
                                                brush = Brush.horizontalGradient(
                                                    listOf(
                                                        GradientStart,
                                                        GradientEnd
                                                    )
                                                )
                                            )
                                        )
                                        Text(
                                            "Time: $duration",
                                            color = Grey1,
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Normal,
                                            maxLines = 1
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.MoreVert,
                                        contentDescription = "Icon",
                                        tint = Grey1,
                                        modifier = Modifier.align(alignment = Alignment.Top)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                         }
                }
            }


                }
        }
    }



