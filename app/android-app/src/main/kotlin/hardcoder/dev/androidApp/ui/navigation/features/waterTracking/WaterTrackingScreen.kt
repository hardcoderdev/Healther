package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.WaterTracking
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import org.koin.androidx.compose.koinViewModel

class WaterTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<WaterTrackingViewModel>()

        WaterTracking(
            viewModel = viewModel,
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