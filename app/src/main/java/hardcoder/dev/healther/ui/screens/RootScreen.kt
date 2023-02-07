package hardcoder.dev.healther.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hardcoder.dev.healther.ui.screens.dashboard.DashboardScreen
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingScreen
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackScreen
import hardcoder.dev.healther.ui.screens.welcome.EnterExerciseStressScreen
import hardcoder.dev.healther.ui.screens.welcome.EnterWeightScreen
import hardcoder.dev.healther.ui.screens.welcome.SelectGenderScreen
import hardcoder.dev.healther.ui.screens.welcome.WelcomeScreen

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable(route = "welcome") {
            WelcomeScreen(
                onStart = {
                    navController.navigate("select_gender")
                },
                onSkip = {
                    navController.navigate("dashboard")
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
                    navController.navigate("save_water_track")
                }
            )
        }
        composable("save_water_track") {
            SaveWaterTrackScreen(
                onGoBack = navController::popBackStack,
                onSaved = navController::popBackStack
            )
        }
    }
}