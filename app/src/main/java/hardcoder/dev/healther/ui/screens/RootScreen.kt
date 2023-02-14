package hardcoder.dev.healther.ui.screens

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hardcoder.dev.healther.ui.screens.dashboard.DashboardScreen
import hardcoder.dev.healther.ui.screens.launch.SplashScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress.EnterExerciseStressScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.gender.SelectGenderScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.weight.EnterWeightScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.welcome.WelcomeScreen
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingScreen
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackScreen
import hardcoder.dev.healther.ui.screens.waterTracking.history.WaterTrackingHistoryScreen
import hardcoder.dev.healther.ui.screens.waterTracking.update.UpdateWaterTrackScreen

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Root.route) {
        composable(route = Screen.Root.route) {
            SplashScreen(
                onStart = {
                    navController.navigate(Screen.Welcome.route)
                },
                onSkip = {
                    navController.navigate(Screen.WaterTrackingFeature.route)
                }
            )
        }
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onStart = {
                    navController.navigate(Screen.SelectGender.route)
                }
            )
        }
        composable(route = Screen.SelectGender.route) {
            SelectGenderScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate(Screen.EnterWeight.route)
                }
            )
        }
        composable(route = Screen.EnterWeight.route) {
            EnterWeightScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate(Screen.EnterExerciseStress.route)
                }
            )
        }
        composable(route = Screen.EnterExerciseStress.route) {
            EnterExerciseStressScreen(
                onGoBack = navController::popBackStack,
                onGoForward = {
                    navController.navigate(Screen.WaterTrackingFeature.route)
                }
            )
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen()
        }
        composable(route = Screen.WaterTrackingFeature.route) {
            WaterTrackingScreen(
                onGoBack = navController::popBackStack,
                onSaveWaterTrack = {
                    navController.navigate(Screen.SaveWaterTrack.route)
                },
                onUpdateWaterTrack = {
                    navController.navigate(Screen.UpdateWaterTrack.buildRoute(it.id))
                },
                onHistoryDetails = {
                    navController.navigate(Screen.WaterTrackingHistory.route)
                }
            )
        }
        composable(route = Screen.SaveWaterTrack.route) {
            SaveWaterTrackScreen(
                onGoBack = navController::popBackStack,
                onSaved = navController::popBackStack
            )
        }
        composable(
            route = Screen.UpdateWaterTrack.route,
            arguments = Screen.UpdateWaterTrack.arguments
        ) { backStackEntry ->
            UpdateWaterTrackScreen(
                waterTrackId = Screen.UpdateWaterTrack.getWaterTrackId(backStackEntry.arguments),
                onGoBack = navController::popBackStack,
                onSaved = navController::popBackStack
            )
        }
        composable(route = Screen.WaterTrackingHistory.route) {
            WaterTrackingHistoryScreen(
                onGoBack = navController::popBackStack,
                onTrackUpdate = {
                    navController.navigate(Screen.UpdateWaterTrack.buildRoute(it.id))
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Root : Screen("splash")
    object Welcome : Screen("welcome")
    object SelectGender : Screen("select_gender")
    object EnterWeight : Screen("enter_weight")
    object EnterExerciseStress : Screen("enter_exercise_stress")
    object Dashboard : Screen("dashboard")
    object WaterTrackingFeature : Screen("water_tracking_feature")
    object WaterTrackingHistory : Screen("water_tracking_history")
    object SaveWaterTrack : Screen("save_water_track")
    object UpdateWaterTrack : Screen("update_water_track/{waterTrackId}") {
        fun buildRoute(waterTrackId: Int) = "update_water_track/$waterTrackId"
        fun getWaterTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("waterTrackId")
        val arguments = listOf(navArgument("waterTrackId") { type = NavType.IntType })
    }
}