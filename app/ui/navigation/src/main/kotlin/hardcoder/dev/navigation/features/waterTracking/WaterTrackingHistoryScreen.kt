package hardcoder.dev.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.history.WaterTrackingHistory

class WaterTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WaterTrackingHistoryViewModel>()

        WaterTrackingHistory(
            dateRangeInputController = viewModel.dateRangeInputController,
            waterTracksLoadingController = viewModel.waterTracksLoadingController,
            onGoBack = navigator::pop,
        )
    }
}