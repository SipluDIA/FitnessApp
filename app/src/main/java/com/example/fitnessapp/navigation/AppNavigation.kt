package com.example.fitnessapp.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnessapp.ui.screen.ActivityScreen
import com.example.fitnessapp.ui.screen.DashboardScreen
import com.example.fitnessapp.ui.screen.LoginScreen
import com.example.fitnessapp.ui.screen.ProfileUpdate
import com.example.fitnessapp.ui.screen.SignUpScreen

@RequiresApi(Build.VERSION_CODES.Q)
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
            route = "profile/{userId}/{userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            val name = backStackEntry.arguments!!.getString("userName")!!
            ProfileUpdate(userId = id, userName = name, navController = navController)
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
            DashboardScreen(userId = id, userName = name, navController = navController)
        }
        composable(
            route = "activity/{userId}/{userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            val name = backStackEntry.arguments!!.getString("userName")!!

            ActivityScreen(userId = id, userName = name, navController = navController)
        }

    }
}