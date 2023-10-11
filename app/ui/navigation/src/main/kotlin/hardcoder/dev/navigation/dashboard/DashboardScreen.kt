package hardcoder.dev.navigation.dashboard

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.navigation.features.diary.DiaryCreationScreen
import hardcoder.dev.navigation.features.diary.DiaryScreen
import hardcoder.dev.navigation.features.foodTracking.foodTracks.FoodTrackingCreationScreen
import hardcoder.dev.navigation.features.foodTracking.foodTracks.FoodTrackingScreen
import hardcoder.dev.navigation.features.moodTracking.MoodTrackingCreationScreen
import hardcoder.dev.navigation.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.navigation.features.pedometer.PedometerScreen
import hardcoder.dev.navigation.features.waterTracking.WaterTrackingCreationScreen
import hardcoder.dev.navigation.features.waterTracking.WaterTrackingScreen
import hardcoder.dev.navigation.settings.SettingsScreen
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.screens.dashboard.Dashboard

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DashboardViewModel>()

        Dashboard(
            featureItemsLoadingController = viewModel.featuresLoadingController,
            pedometerToggleController = viewModel.pedometerToggleController,
            onGoToWaterTrackingFeature = {
                navigator += WaterTrackingScreen()
            },
            onCreateWaterTrack = {
                navigator += WaterTrackingCreationScreen()
            },
            onGoToFoodTrackingFeature = {
                navigator += FoodTrackingScreen()
            },
            onCreateFoodTrack = {
                navigator += FoodTrackingCreationScreen()
            },
            onGoToMoodTrackingFeature = {
                navigator += MoodTrackingScreen()
            },
            onCreateMoodTrack = {
                navigator += MoodTrackingCreationScreen()
            },
            onGoToPedometerFeature = {
                navigator += PedometerScreen()
            },
            onGoToDiary = {
                navigator += DiaryScreen()
            },
            onCreateDiaryTrack = {
                navigator += DiaryCreationScreen()
            },
        ) {
            navigator += SettingsScreen()
        }
    }
}