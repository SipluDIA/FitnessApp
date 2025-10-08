package com.example.fitnessapp.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitnessapp.network.NetworkManager
import com.example.fitnessapp.offline.AppDatabase
import com.example.fitnessapp.offline.OfflineActivity
import com.example.fitnessapp.offline.OfflineSyncManager
import android.net.ConnectivityManager
import com.example.fitnessapp.R
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import com.example.fitnessapp.ui.theme.GradientEnd
import com.example.fitnessapp.ui.theme.GradientStart
import com.example.fitnessapp.ui.theme.poppinsFamily
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Extension to retrieve Activity from LocalContext
fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> throw IllegalStateException("Activity not found")
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ActivityScreen(navController: NavHostController, userId: Int) {
    // Connectivity observer for sync
    val context = LocalContext.current
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Try to sync when network is available
                OfflineSyncManager.syncOfflineActivities(context)
            }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        try {
            connectivityManager.registerNetworkCallback(request, networkCallback)
        } catch (e: SecurityException) {
            // Permission missing, do nothing
        }
        onDispose {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            } catch (_: Exception) {}
        }
    }
    val activity = context.findActivity()
    // Permission state
    var hasPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    // Sensor & tracking state
    var stepCount by remember { mutableStateOf(0) }
    var initialCount by remember { mutableStateOf(0L) }
    var lastTotalCount by remember { mutableStateOf(0L) }
    var isTracking by remember { mutableStateOf(false) }
    var activityType by remember { mutableStateOf("Walking") }
    var startTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var endTime by remember { mutableStateOf<LocalDateTime?>(null) }

    // Sensor setup
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val total = event.values[0].toLong()
                lastTotalCount = total
                if (isTracking) {
                    stepCount = (total - initialCount).toInt()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }
    DisposableEffect(stepSensor) {
        stepSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val formatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Activity", color = Color.Black) },
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Count:",fontSize = 14.sp, fontFamily = poppinsFamily, fontWeight = FontWeight.SemiBold,)
            Box(contentAlignment = Alignment.Center){
                Image(
                    painter = rememberDrawablePainter(
                        drawable = getDrawable(
                            LocalContext.current,
                            R.drawable.circle
                        )
                    ),
                    contentDescription = "Loading animation",
                    contentScale = ContentScale.FillWidth,
                )

                Spacer(Modifier.height(16.dp))
                Text("$stepCount", fontSize = 60.sp, fontFamily = poppinsFamily, fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                GradientStart,
                                GradientEnd
                            )
                        )))
            }
            Spacer(Modifier.height(16.dp))
            Text("Select Activity", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Row {
                OutlinedButton(
                    onClick = { activityType = "Walking" },
                    enabled = activityType != "Walking"
                ) { Text("Walking") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { activityType = "Running" },
                    enabled = activityType != "Running"
                ) { Text("Running") }
            }
            Row {
                OutlinedButton(
                    onClick = { activityType = "Cycling" },
                    enabled = activityType != "Cycling"
                ) { Text("Cycling") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { activityType = "Swimming" },
                    enabled = activityType != "Swimming"
                ) { Text("Swimming") }
            }
            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    if (hasPermission && stepSensor != null) {
                        initialCount = lastTotalCount
                        startTime = LocalDateTime.now()
                        isTracking = true
                    } else {
                        Toast.makeText(
                            context,
                            "Permission required or sensor unavailable",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, enabled = !isTracking) { Text("Start") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (isTracking) {
                        // Pause
                        isTracking = false
                    } else {
                        // Resume
                        initialCount = lastTotalCount - stepCount
                        isTracking = true
                    }
                }, enabled = startTime != null) {
                    Text(if (isTracking) "Pause" else "Resume")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (startTime != null) {
                        isTracking = false
                        endTime = LocalDateTime.now()
                        // Save activity, supporting offline
                        val isOnline = try {
                            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                            val network = cm?.activeNetwork
                            val capabilities = cm?.getNetworkCapabilities(network)
                            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        } catch (e: Exception) { false }

                        if (isOnline) {
                            NetworkManager.saveActivity(
                                userId = userId,
                                activityType = activityType,
                                stepCount = stepCount,
                                startTime = formatter.format(startTime!!),
                                endTime = formatter.format(endTime!!)
                            ) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    navController.navigate("summery/$userId")
                                }
                            }
                        } else {
                            // Save to Room for later sync
                            CoroutineScope(Dispatchers.IO).launch {
                                val db = AppDatabase.getDatabase(context)
                                db.offlineActivityDao().insert(
                                    OfflineActivity(
                                        userId = userId,
                                        activityType = activityType,
                                        stepCount = stepCount,
                                        startTime = formatter.format(startTime!!),
                                        endTime = formatter.format(endTime!!),
                                        synced = false
                                    )
                                )
                            }
                            Toast.makeText(context, "Saved offline. Will sync when online.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, enabled = startTime != null) { Text("Save") }
            }
        }
    }
}