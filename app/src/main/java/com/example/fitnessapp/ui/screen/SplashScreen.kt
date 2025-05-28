package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val gradiantBackground = Brush.horizontalGradient(
        0.0f to Color(red = 237, green = 118, blue = 94),
        1.0f to Color(red = 254, green = 168, blue = 88),
        startX = 0.0f,
        endX = 1000.0f
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradiantBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.hello), fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
    LaunchedEffect(Unit) {
        delay(2000)
    }
}