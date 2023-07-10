package hardcoder.dev.androidApp.ui.navigation.dashboard

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.diary.DiaryScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.features.pedometer.PedometerScreen
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.WaterTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.settings.SettingsScreen
import hardcoder.dev.androidApp.ui.screens.dashboard.Dashboard
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DashboardViewModel>()

        Dashboard(
            viewModel = viewModel,
            onGoToDiary = {
                navigator += DiaryScreen()
            },
            onGoToWaterTrackingFeature = {
                navigator += WaterTrackingScreen()
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
            onGoToSettings = {
                navigator += SettingsScreen()
            },
        )
    }
}