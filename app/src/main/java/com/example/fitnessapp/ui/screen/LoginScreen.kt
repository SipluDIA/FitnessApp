package com.example.fitnessapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R
import com.example.fitnessapp.network.NetworkManager
import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.Black
import com.example.fitnessapp.ui.theme.GradientEnd
import com.example.fitnessapp.ui.theme.GradientStart
import com.example.fitnessapp.ui.theme.Grey2
import com.example.fitnessapp.ui.theme.Grey3
import com.example.fitnessapp.ui.theme.Pink1
import com.example.fitnessapp.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: (userId: Int, userName: String) -> Unit
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hey there,",
            color = Black,
            fontSize = 16.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Login to your Account",
            color = Black,
            fontSize = 20.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))


        TextField(
            singleLine = true,
            value = username,
            onValueChange = { username = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = {
                Text(
                    text = "User Name",
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
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = {
                Text(
                    text = "Password",
                    color = Grey3,
                    fontSize = 12.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_leading_password),
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)

                .background(
                    brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)),
                    shape = ButtonDefaults.shape
                )
                .height(55.dp),
            onClick = {
                NetworkManager.login(username, password) { success, userId, userNameOrMessage ->
                    if (success) {
                        // userId is likely a String, convert to Int if needed
                        val id = userId.toString().toIntOrNull() ?: 0
                        onLoginSuccess(id, userNameOrMessage)
                    } else {
                        errorMessage = userNameOrMessage
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                "Login", color = Color.White, fontSize = 16.sp,
                fontFamily = poppinsFamily, fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToSignUp) {
            Text("Don't have an account? Sign Up")
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
