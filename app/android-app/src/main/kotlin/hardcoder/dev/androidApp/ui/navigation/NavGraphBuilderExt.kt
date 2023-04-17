package hardcoder.dev.androidApp.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import hardcoder.dev.androidApp.ui.dashboard.diary.create.DiaryCreateTrackScreen
import hardcoder.dev.androidApp.ui.dashboard.diary.DiaryScreen
import hardcoder.dev.androidApp.ui.dashboard.diary.tags.ManageTagsScreen
import hardcoder.dev.androidApp.ui.dashboard.diary.tags.create.CreateTagScreen
import hardcoder.dev.androidApp.ui.dashboard.diary.tags.update.UpdateTagScreen
import hardcoder.dev.androidApp.ui.dashboard.diary.update.DiaryUpdateTrackScreen
import hardcoder.dev.androidApp.ui.features.fasting.FastingScreen
import hardcoder.dev.androidApp.ui.features.fasting.create.FastingCreationTrackScreen
import hardcoder.dev.androidApp.ui.features.fasting.history.FastingHistoryScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.ManageActivitiesScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.create.CreateActivityScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.update.UpdateActivityScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.create.CreateMoodTrackScreen
import hardcoder.dev.androidApp.ui.features.moodTracking.history.MoodTrackingHistoryScreen
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
import hardcoder.dev.logic.hero.gender.Gender

fun NavGraphBuilder.setUpDestinations(navController: NavController) {
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
                navController.navigate(Screen.Diary.route)
            }
        )
    }
}

fun NavGraphBuilder.waterTrackingDestinations(navController: NavController) {
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

fun NavGraphBuilder.pedometerDestinations(navController: NavController) {
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

fun NavGraphBuilder.fastingDestinations(navController: NavController) {
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

fun NavGraphBuilder.moodTrackingDestinations(navController: NavController) {
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
            onManageActivities = { navController.navigate(Screen.ManageHobbies.route) },
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
        ManageActivitiesScreen(
            onGoBack = navController::popBackStack,
            onCreateActivity = { navController.navigate(Screen.ActivityCreate.route) },
            onUpdateActivity = { navController.navigate(Screen.ActivityUpdate.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.ActivityCreate.route) {
        CreateActivityScreen(
            onGoBack = navController::popBackStack
        )
    }
    composable(
        route = Screen.ActivityUpdate.route,
        arguments = Screen.ActivityUpdate.arguments
    ) {
        UpdateActivityScreen(
            activityId = Screen.ActivityUpdate.getActivityIdId(it.arguments),
            onGoBack = navController::popBackStack
        )
    }
}

fun NavGraphBuilder.diaryDestinations(navController: NavController) {
    composable(route = Screen.Diary.route) {
        DiaryScreen(
            onGoBack = navController::popBackStack,
            onCreateTrack = { navController.navigate(Screen.DiaryCreateTrack.route) },
            onUpdateTrack = { navController.navigate(Screen.DiaryUpdateTrack.buildRoute(it)) }
        )
    }
    composable(route = Screen.DiaryCreateTrack.route) {
        DiaryCreateTrackScreen(
            onGoBack = navController::popBackStack,
            onManageTags = { navController.navigate(Screen.ManageDiaryTags.route) }
        )
    }
    composable(
        route = Screen.DiaryUpdateTrack.route,
        arguments = Screen.DiaryUpdateTrack.arguments
    ) {
        DiaryUpdateTrackScreen(
            onGoBack = navController::popBackStack,
            diaryTrackId = Screen.DiaryUpdateTrack.getDiaryTrackId(it.arguments),
            onManageTags = { navController.navigate(Screen.ManageDiaryTags.route) }
        )
    }
    composable(route = Screen.ManageDiaryTags.route) {
        ManageTagsScreen(
            onGoBack = navController::popBackStack,
            onCreateTag = { navController.navigate(Screen.CreateDiaryTag.route) },
            onUpdateTag = { navController.navigate(Screen.UpdateDiaryTag.buildRoute(it.id)) }
        )
    }
    composable(route = Screen.CreateDiaryTag.route) {
        CreateTagScreen(onGoBack = navController::popBackStack)
    }
    composable(
        route = Screen.UpdateDiaryTag.route,
        arguments = Screen.UpdateDiaryTag.arguments
    ) {
        UpdateTagScreen(
            tagId = Screen.UpdateDiaryTag.getDiaryTagId(it.arguments),
            onGoBack = navController::popBackStack
        )
    }
}