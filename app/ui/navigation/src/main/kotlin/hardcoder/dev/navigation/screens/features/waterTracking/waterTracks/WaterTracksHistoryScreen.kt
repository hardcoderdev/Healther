package hardcoder.dev.navigation.screens.features.waterTracking.waterTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.history.WaterTrackingHistory
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun WaterTracksHistoryScreen(navController: NavController) {
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val viewModel = koinViewModel<WaterTrackingHistoryViewModel>()

    WaterTrackingHistory(
        dateTimeProvider = dateTimeProvider,
        dateRangeInputController = viewModel.dateRangeInputController,
        waterTracksLoadingController = viewModel.waterTracksLoadingController,
        onGoBack = navController::popBackStack,
    )
}