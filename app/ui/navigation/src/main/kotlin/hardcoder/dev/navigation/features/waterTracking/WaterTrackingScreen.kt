package hardcoder.dev.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.WaterTracking

class WaterTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WaterTrackingViewModel>()

        WaterTracking(
            waterTracksLoadingController = viewModel.waterTracksLoadingController,
            millilitersDrunkLoadingController = viewModel.millilitersDrunkLoadingController,
            progressController = viewModel.dailyRateProgressController,
            onGoBack = navigator::pop,
            onCreateWaterTrack = {
                navigator += WaterTrackingCreationScreen()
            },
            onUpdateWaterTrack = { waterTrackId ->
                navigator += WaterTrackingUpdateScreen(waterTrackId)
            },
            onGoToHistory = {
                navigator += WaterTrackingHistoryScreen()
            },
            onGoToAnalytics = {
                navigator += WaterTrackingAnalyticsScreen()
            },
        )
    }
}