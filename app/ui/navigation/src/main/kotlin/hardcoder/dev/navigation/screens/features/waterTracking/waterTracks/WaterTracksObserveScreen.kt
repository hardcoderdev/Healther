package hardcoder.dev.navigation.screens.features.waterTracking.waterTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.WaterTracking
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun WaterTracksObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<WaterTrackingViewModel>()

    WaterTracking(
        waterTracksLoadingController = viewModel.waterTracksLoadingController,
        millilitersDrunkLoadingController = viewModel.millilitersDrunkLoadingController,
        progressController = viewModel.dailyRateProgressController,
        onGoBack = navController::popBackStack,
        onCreateWaterTrack = {
            navController.navigate(Screen.WaterTracksCreate.route)
        },
        onUpdateWaterTrack = { waterTrackId ->
            navController.navigate(Screen.WaterTracksUpdate.buildRoute(waterTrackId))
        },
        onGoToHistory = {
            navController.navigate(Screen.WaterTracksHistory.route)
        },
        onGoToAnalytics = {
            navController.navigate(Screen.WaterTracksAnalytics.route)
        },
    )
}