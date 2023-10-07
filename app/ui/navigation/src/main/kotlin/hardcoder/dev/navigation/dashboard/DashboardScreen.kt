package hardcoder.dev.navigation.dashboard

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.diary.DiaryCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.diary.DiaryScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.MoodTrackingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.features.pedometer.PedometerScreen
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.WaterTrackingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.WaterTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.settings.SettingsScreen
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.screens.dashboard.Dashboard
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DashboardViewModel>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

        Dashboard(
            millisDistanceFormatter = millisDistanceFormatter,
            featureItemsLoadingController = viewModel.featuresLoadingController,
            //pedometerToggleController = viewModel.pedometerToggleController, TODO UNCOMMENT
            pedometerToggleController = MockControllersProvider.toggleController(),
            onGoToWaterTrackingFeature = {
                navigator += WaterTrackingScreen()
            },
            onCreateWaterTrack = {
                navigator += WaterTrackingCreationScreen()
            },
            onGoToPedometerFeature = {
                navigator += PedometerScreen()
            },
            onGoToFastingFeature = {
                navigator += FastingScreen()
            },
            onStartFasting = {
                navigator += FastingCreationScreen()
            },
            onGoToMoodTrackingFeature = {
                navigator += MoodTrackingScreen()
            },
            onCreateMoodTrack = {
                navigator += MoodTrackingCreationScreen()
            },
            onGoToDiary = {
                navigator += DiaryScreen()
            },
            onCreateDiaryTrack = {
                navigator += DiaryCreationScreen()
            },
            onGoToSettings = {
                navigator += SettingsScreen()
            },
        )
    }
}