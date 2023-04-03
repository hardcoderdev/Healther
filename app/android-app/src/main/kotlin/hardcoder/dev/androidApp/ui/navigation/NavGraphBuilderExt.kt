package hardcoder.dev.androidApp.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hardcoder.dev.androidApp.ui.features.fasting.FastingScreen
import hardcoder.dev.androidApp.ui.features.fasting.create.FastingCreationTrackScreen
import hardcoder.dev.androidApp.ui.features.fasting.history.FastingHistoryScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.create.CreateMoodTrackScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.history.MoodTrackingHistoryScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.hobby.ManageHobbyScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.hobby.create.CreateHobbyTrackScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.hobby.update.UpdateHobbyTrackScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.ManageMoodTypeScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.create.CreateMoodTypeScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.update.UpdateMoodTypeScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.update.UpdateMoodTrackScreen
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerScreen
import hardcoder.dev.androidApp.ui.features.pedometer.history.PedometerHistoryScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.DrinkTypeManageScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.create.CreateDrinkTypeScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.update.UpdateDrinkTypeScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.WaterTrackingScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.create.CreateWaterTrackScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.history.WaterTrackingHistoryScreen
import hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack.update.UpdateWaterTrackScreen
import hardcoder.dev.androidApp.ui.setUpFlow.exerciseStress.EnterExerciseStressScreen
import hardcoder.dev.androidApp.ui.setUpFlow.gender.SelectGenderScreen
import hardcoder.dev.androidApp.ui.setUpFlow.weight.EnterWeightScreen
import hardcoder.dev.androidApp.ui.setUpFlow.welcome.WelcomeScreen
import hardcoder.dev.logic.entities.hero.Gender

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
                navController.navigate(Screen.WaterTrackingFeature.route)
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
        CreateWaterTrackScreen(
            onGoBack = navController::popBackStack,
            onManageDrinkType = { navController.navigate(Screen.ManageDrinkTypes.route) }
        )
    }
    composable(
        route = Screen.UpdateWaterTrack.route,
        arguments = Screen.UpdateWaterTrack.arguments
    ) { backStackEntry ->
        UpdateWaterTrackScreen(
            waterTrackId = Screen.UpdateWaterTrack.getWaterTrackId(backStackEntry.arguments),
            onGoBack = navController::popBackStack,
            onManageDrinkType = { navController.navigate(Screen.ManageDrinkTypes.route) }
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
    composable(route = Screen.ManageDrinkTypes.route) {
        DrinkTypeManageScreen(
            onGoBack = navController::popBackStack,
            onCreateDrinkType = { navController.navigate(Screen.CreateDrinkType.route) },
            onUpdateDrinkType = { navController.navigate(Screen.UpdateDrinkType.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.CreateDrinkType.route) {
        CreateDrinkTypeScreen(
            onGoBack = navController::popBackStack
        )
    }
    composable(
        route = Screen.UpdateDrinkType.route,
        arguments = Screen.UpdateDrinkType.arguments
    ) {
        UpdateDrinkTypeScreen(
            drinkTypeId = Screen.UpdateDrinkType.getDrinkTypeId(it.arguments),
            onGoBack = navController::popBackStack
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
            onGoToPedometerHistory = { navController.navigate(Screen.PedometerHistory.route) }
        )
    }
    composable(route = Screen.PedometerHistory.route) {
        PedometerHistoryScreen(onGoBack = navController::popBackStack)
    }
}

fun NavGraphBuilder.addFastingDestinations(navController: NavController) {
    composable(route = Screen.FastingFeature.route) {
        FastingScreen(
            onGoBack = navController::popBackStack,
            onCreateTrack = { navController.navigate(Screen.FastingCreateTrack.route) },
            onHistoryDetails = { navController.navigate(Screen.FastingHistory.route) }
        )
    }
    composable(route = Screen.FastingCreateTrack.route) {
        FastingCreationTrackScreen(
            onGoBack = navController::popBackStack
        )
    }
    composable(route = Screen.FastingHistory.route) {
        FastingHistoryScreen(
            onGoBack = navController::popBackStack
        )
    }
}

fun NavGraphBuilder.addMoodTrackingDestinations(navController: NavController) {
    composable(route = Screen.MoodTrackingFeature.route) {
        MoodTrackingScreen(
            onGoBack = navController::popBackStack,
            onCreateMoodTrack = { navController.navigate(Screen.MoodTrackingCreate.route) },
            onGoToHistory = { navController.navigate(Screen.MoodTrackingHistory.route) },
            onUpdate = { navController.navigate(Screen.MoodTrackingUpdate.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.MoodTrackingHistory.route) {
        MoodTrackingHistoryScreen(
            onGoBack = navController::popBackStack,
            onTrackUpdate = { navController.navigate(Screen.MoodTrackingUpdate.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.MoodTrackingCreate.route) {
        CreateMoodTrackScreen(
            onGoBack = navController::popBackStack,
            onManageHobby = { navController.navigate(Screen.ManageHobbies.route) },
            onManageMoodTypes = { navController.navigate(Screen.ManageMoodTypes.route) }
        )
    }
    composable(
        route = Screen.MoodTrackingUpdate.route,
        arguments = Screen.MoodTrackingUpdate.arguments
    ) {
        UpdateMoodTrackScreen(
            moodTrackId = Screen.MoodTrackingUpdate.getMoodTrackId(it.arguments),
            onManageHobby = { navController.navigate(Screen.ManageHobbies.route) },
            onManageMoodTypes = { navController.navigate(Screen.ManageMoodTypes.route) },
            onGoBack = navController::popBackStack
        )
    }
    composable(route = Screen.ManageMoodTypes.route) {
        ManageMoodTypeScreen(
            onGoBack = navController::popBackStack,
            onCreateMoodType = { navController.navigate(Screen.MoodTypeCreate.route) },
            onUpdateMoodType = { navController.navigate(Screen.MoodTypeUpdate.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.MoodTypeCreate.route) {
        CreateMoodTypeScreen(onGoBack = navController::popBackStack)
    }
    composable(
        route = Screen.MoodTypeUpdate.route,
        arguments = Screen.MoodTypeUpdate.arguments
    ) {
        UpdateMoodTypeScreen(
            moodTypeId = Screen.MoodTypeUpdate.getMoodTypeId(it.arguments),
            onGoBack = navController::popBackStack
        )
    }
    composable(route = Screen.ManageHobbies.route) {
        ManageHobbyScreen(
            onGoBack = navController::popBackStack,
            onCreateHobbyTrack = { navController.navigate(Screen.HobbyCreate.route) },
            onUpdateHobbyTrack = { navController.navigate(Screen.HobbyUpdate.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.HobbyCreate.route) {
        CreateHobbyTrackScreen(
            onGoBack = navController::popBackStack
        )
    }
    composable(
        route = Screen.HobbyUpdate.route,
        arguments = Screen.HobbyUpdate.arguments
    ) {
        UpdateHobbyTrackScreen(
            hobbyTrackId = Screen.HobbyUpdate.getHobbyTrackId(it.arguments),
            onGoBack = navController::popBackStack
        )
    }
}