package hardcoder.dev.healther.ui.screens

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hardcoder.dev.healther.entities.Gender
import hardcoder.dev.healther.ui.screens.dashboard.DashboardScreen
import hardcoder.dev.healther.ui.screens.splash.SplashScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress.EnterExerciseStressScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.gender.SelectGenderScreen
import hardcoder.dev.healther.ui.screens.setUpFlow.heroCreate.HeroCreateScreen
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
                onStartSetUp = {
                    navController.navigate(Screen.Welcome.route)
                },
                onNavigateToDashboard = {
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
                    navController.navigate(Screen.EnterWeight.buildRoute(it))
                }
            )
        }
        composable(
            route = Screen.EnterWeight.route,
            arguments = Screen.EnterWeight.arguments
        ) { backStackEntry ->
            EnterWeightScreen(
                gender = Gender.valueOf(Screen.EnterWeight.getGender(backStackEntry.arguments)),
                onGoBack = navController::popBackStack,
                onGoForward = { gender, weight ->
                    navController.navigate(Screen.EnterExerciseStress.buildRoute(
                        gender = gender,
                        weight = weight
                    ))
                }
            )
        }
        composable(
            route = Screen.EnterExerciseStress.route,
            arguments = Screen.EnterExerciseStress.arguments
        ) {
            EnterExerciseStressScreen(
                gender = Gender.valueOf(Screen.EnterExerciseStress.getGender(it.arguments)),
                weight = Screen.EnterExerciseStress.getWeight(it.arguments),
                exerciseStressTime = Screen.EnterExerciseStress.getExerciseStressTime(it.arguments),
                onGoBack = navController::popBackStack,
                onGoForward = { gender, weight, exerciseStressTime ->
                    navController.navigate(Screen.HeroCreate.buildRoute(
                        gender = gender,
                        weight = weight,
                        exerciseStressTime = exerciseStressTime
                    ))
                }
            )
        }
        composable(
            route = Screen.HeroCreate.route,
            arguments = Screen.HeroCreate.arguments
        ) {
            HeroCreateScreen(
                gender = Gender.valueOf(Screen.HeroCreate.getGender(it.arguments)!!),
                weight = Screen.HeroCreate.getWeight(it.arguments),
                exerciseStressTime = Screen.HeroCreate.getExerciseStressTime(it.arguments),
                onGoToDashboard = {
                    //navController.navigate(Screen.Dashboard.route)
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
    object EnterWeight : Screen("enter_weight/{gender}") {
        fun buildRoute(gender: Gender) = "enter_weight/${gender.name}"
        fun getGender(arguments: Bundle?) = requireNotNull(arguments).getString("gender")!!
        val arguments = listOf(navArgument("gender") { type = NavType.StringType })
    }
    object EnterExerciseStress : Screen("enter_exercise_stress/{gender}/{weight}") {
        fun buildRoute(gender: Gender, weight: Int) = "enter_exercise_stress/${gender.name}/${weight}"
        fun getGender(arguments: Bundle?) = requireNotNull(arguments).getString("gender")!!
        fun getWeight(arguments: Bundle?) = requireNotNull(arguments).getInt("weight")
        fun getExerciseStressTime(arguments: Bundle?) = requireNotNull(arguments).getInt("exerciseStressTime")
        val arguments = listOf(
            navArgument("gender") { type = NavType.StringType },
            navArgument("weight") { type = NavType.IntType }
        )
    }
    object Dashboard : Screen("dashboard")
    object HeroCreate : Screen("hero_create/{gender}/{weight}/{exerciseStressTime}") {
        fun buildRoute(gender: Gender, weight: Int, exerciseStressTime: Int) =
            "hero_create/${gender.name}/$weight/$exerciseStressTime"
        fun getGender(arguments: Bundle?) = requireNotNull(arguments).getString("gender")
        fun getWeight(arguments: Bundle?) = requireNotNull(arguments).getInt("weight")
        fun getExerciseStressTime(arguments: Bundle?) = requireNotNull(arguments).getInt("exerciseStressTime")
        val arguments = listOf(
            navArgument("gender") { type = NavType.StringType },
            navArgument("weight") { type = NavType.IntType },
            navArgument("exerciseStressTime") { type = NavType.IntType }
        )
    }
    object WaterTrackingFeature : Screen("water_tracking_feature")
    object WaterTrackingHistory : Screen("water_tracking_history")
    object SaveWaterTrack : Screen("save_water_track")
    object UpdateWaterTrack : Screen("update_water_track/{waterTrackId}") {
        fun buildRoute(waterTrackId: Int) = "update_water_track/$waterTrackId"
        fun getWaterTrackId(arguments: Bundle?) = requireNotNull(arguments).getInt("waterTrackId")
        val arguments = listOf(navArgument("waterTrackId") { type = NavType.IntType })
    }
}