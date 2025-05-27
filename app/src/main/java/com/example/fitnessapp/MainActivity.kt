package com.example.fitnessapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnessapp.DashboardScreen
import com.example.fitnessapp.LoginScreen
import com.example.fitnessapp.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.init(applicationContext)
        setContent {
            FitnessApp()
        }
    }
}

@Composable
fun FitnessApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = { userId, userName ->
                    // Navigate to dashboard, pass ID & encoded username
                    val encodedName = Uri.encode(userName)
                    navController.navigate("dashboard/$userId/$encodedName")
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = { userId, userName ->
                    val encodedName = Uri.encode(userName)
                    navController.navigate("dashboard/$userId/$encodedName")
                }
            )
        }
        composable(
            route = "dashboard/{userId}/{userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            val name = backStackEntry.arguments!!.getString("userName")!!
            DashboardScreen(userId = id, userName = name)
        }
    }
}


