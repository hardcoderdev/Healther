package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics.WaterTrackingAnalytics
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import org.koin.androidx.compose.koinViewModel

class WaterTrackingAnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<WaterTrackingAnalyticsViewModel>()

        WaterTrackingAnalytics(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}