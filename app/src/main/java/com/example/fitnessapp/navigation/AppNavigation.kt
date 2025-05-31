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
import com.example.fitnessapp.ui.screen.ViewProgress
import com.example.fitnessapp.ui.screen.YourGoal

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun FitnessApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = { userId, _ ->
                    // Navigate to dashboard with userId
                    navController.navigate("dashboard/$userId")
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = { userId, _ ->
                    // Navigate to dashboard with userId
                    navController.navigate("dashboard/$userId")
                }
            )
        }
        composable(
            route = "profile/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            ProfileUpdate(userId = id, navController = navController)
        }
        composable(
            route = "dashboard/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            DashboardScreen(userId = id, navController = navController)
        }
        composable(
            route = "activity/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            ActivityScreen(userId = id,navController = navController)
        }
        composable(
            route = "goal/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            YourGoal(userId = id,navController = navController)
        }
        composable(
            route = "progress/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("userId")
            ViewProgress(userId = id,navController = navController)
        }

    }
}