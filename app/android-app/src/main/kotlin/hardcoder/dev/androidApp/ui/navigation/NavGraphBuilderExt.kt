package hardcoder.dev.androidApp.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerScreen
import hardcoder.dev.androidApp.ui.features.pedometer.history.PedometerHistoryScreen
import hardcoder.dev.androidApp.ui.features.starvation.StarvationScreen
import hardcoder.dev.androidApp.ui.features.starvation.create.StarvationCreationTrackScreen
import hardcoder.dev.androidApp.ui.features.starvation.history.StarvationHistoryScreen
import hardcoder.dev.androidApp.ui.features.waterBalance.WaterTrackingScreen
import hardcoder.dev.androidApp.ui.features.waterBalance.create.SaveWaterTrackScreen
import hardcoder.dev.androidApp.ui.features.waterBalance.history.WaterTrackingHistoryScreen
import hardcoder.dev.androidApp.ui.features.waterBalance.update.UpdateWaterTrackScreen
import hardcoder.dev.androidApp.ui.setUpFlow.exerciseStress.EnterExerciseStressScreen
import hardcoder.dev.androidApp.ui.setUpFlow.gender.SelectGenderScreen
import hardcoder.dev.androidApp.ui.setUpFlow.weight.EnterWeightScreen
import hardcoder.dev.androidApp.ui.setUpFlow.welcome.WelcomeScreen
import hardcoder.dev.entities.hero.Gender

fun NavGraphBuilder.addSetUpDestinations(navController: NavController) {
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
            gender = Gender.valueOf(
                Screen.EnterWeight.getGender(
                    backStackEntry.arguments
                )
            ),
            onGoBack = navController::popBackStack,
            onGoForward = { gender, weight ->
                navController.navigate(
                    Screen.EnterExerciseStress.buildRoute(
                        gender = gender,
                        weight = weight
                    )
                )
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
            onGoForward = {
                //navController.navigate(Screen.Dashboard.route)
                navController.navigate(Screen.StarvationFeature.route)
            }
        )
    }
}

fun NavGraphBuilder.addWaterTrackingDestinations(navController: NavController) {
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

fun NavGraphBuilder.addPedometerDestinations(navController: NavController) {
    composable(
        route = Screen.PedometerFeature.route,
        deepLinks = Screen.PedometerFeature.deepLinks
    ) {
        PedometerScreen(
            onGoBack = navController::popBackStack,
            onGoToPedometerHistory = {
                navController.navigate(Screen.PedometerHistory.route)
            }
        )
    }
    composable(route = Screen.PedometerHistory.route) {
        PedometerHistoryScreen(onGoBack = navController::popBackStack)
    }
}

fun NavGraphBuilder.addStarvationDestinations(navController: NavController) {
    composable(route = Screen.StarvationFeature.route) {
        StarvationScreen(
            onGoBack = navController::popBackStack,
            onCreateStarvationTrack = { navController.navigate(Screen.StarvationCreateTrack.route) },
            onHistoryDetails = { navController.navigate(Screen.StarvationHistory.route) }
        )
    }
    composable(route = Screen.StarvationCreateTrack.route) {
        StarvationCreationTrackScreen(
            onGoBack = navController::popBackStack
        )
    }
    composable(route = Screen.StarvationHistory.route) {
        StarvationHistoryScreen(
            onGoBack = navController::popBackStack
        )
    }
}