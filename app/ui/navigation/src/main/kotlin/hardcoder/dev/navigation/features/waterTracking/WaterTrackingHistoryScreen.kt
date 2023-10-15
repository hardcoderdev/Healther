package hardcoder.dev.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.history.WaterTrackingHistory
import org.koin.compose.koinInject

class WaterTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WaterTrackingHistoryViewModel>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        WaterTrackingHistory(
            dateTimeProvider = dateTimeProvider,
            dateRangeInputController = viewModel.dateRangeInputController,
            waterTracksLoadingController = viewModel.waterTracksLoadingController,
            onGoBack = navigator::pop,
        )
    }
}