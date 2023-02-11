package hardcoder.dev.healther.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hardcoder.dev.healther.ui.screens.dashboard.DashboardScreen
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingScreen
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackScreen
import hardcoder.dev.healther.ui.screens.waterTracking.history.WaterTrackingHistoryScreen
import hardcoder.dev.healther.ui.screens.welcome.EnterExerciseStressScreen
import hardcoder.dev.healther.ui.screens.welcome.EnterWeightScreen
import hardcoder.dev.healther.ui.screens.welcome.SelectGenderScreen
import hardcoder.dev.healther.ui.screens.welcome.WelcomeScreen
import hardcoder.dev.healther.ui.screens.welcome.WhereWeGo

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "where_we_go") {
        composable(route = "where_we_go") {
            WhereWeGo(
                onStart = {
                    navController.navigate("welcome")
                },
                onSkip = {
                    navController.navigate("water_tracking_feature")
                }
            )
        }
        composable(route = "welcome") {
            WelcomeScreen(
                onStart = {
                    navController.navigate("select_gender")
                }
            )
        }
        composable(route = "select_gender") {
            SelectGenderScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate("enter_weight")
                }
            )
        }
        composable(route = "enter_weight") {
            EnterWeightScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate("enter_exercise_stress")
                }
            )
        }
        composable(route = "enter_exercise_stress") {
            EnterExerciseStressScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate("water_tracking_feature")
                }
            )
        }
        composable(route = "dashboard") {
            DashboardScreen()
        }
        composable(route = "water_tracking_feature") {
            WaterTrackingScreen(
                onGoBack = navController::popBackStack,
                onSaveWaterTrack = {
                    navController.navigate("save_water_track/-1")
                },
                onUpdateWaterTrack = {
                    navController.navigate("save_water_track/${it.id}")
                },
                onHistoryDetails = {
                    navController.navigate("water_tracking_history")
                }
            )
        }
        composable(
            route = "save_water_track/{waterTrackId}",
            arguments = listOf(navArgument("waterTrackId") { type = NavType.IntType })
        ) { backStackEntry ->
            SaveWaterTrackScreen(
                waterTrackId = backStackEntry.arguments?.getInt("waterTrackId") ?: -1,
                onGoBack = navController::popBackStack,
                onSaved = navController::popBackStack
            )
        }
        composable(route = "water_tracking_history") {
            WaterTrackingHistoryScreen(
                onGoBack = navController::popBackStack,
                onTrackUpdate = {
                    navController.navigate("save_water_track/${it.id}")
                }
            )
        }
    }
}