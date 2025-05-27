package com.example.fitnessapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment.Companion as Alignment1

@Composable
fun AsyncImage(model: String, contentDescription: String) {

}

@Composable
fun WelcomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment1.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment1.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.welcome),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.intro_message),
                    fontSize = 20.sp,
                    modifier = Modifier.alpha(.3f)
                )
            }


            Column(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment1.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.height(700.dp),
                    painter = painterResource(R.drawable.fitness_logo),
                    contentDescription = "welcome_screen_image"
                )
            }

            Column(horizontalAlignment = Alignment1.CenterHorizontally) {
                Button(
                    onClick = {  },
                    Modifier
                        .fillMaxWidth()
                        .absolutePadding(bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color(red = 255, green = 125, blue = 0),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Sign in")
                }

                OutlinedButton(
                    onClick = {  },
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .absolutePadding(top = 5.dp)
                ) {
                    Text(
                        text = "Sign up",
                        color = Color(red = 255, green = 125, blue = 0)
                    )

                }
            }
        }
    }
}