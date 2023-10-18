package hardcoder.dev.navigation.screens.features.waterTracking.waterTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import hardcoder.dev.resolvers.features.waterTracking.WaterTrackingStatisticResolver
import hardcoder.dev.screens.features.waterTracking.waterTrack.analytics.WaterTrackingAnalytics
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun WaterTracksAnalyticsScreen(navController: NavController) {
    val waterTrackingStatisticResolver = koinInject<WaterTrackingStatisticResolver>()
    val viewModel = koinViewModel<WaterTrackingAnalyticsViewModel>()

    WaterTrackingAnalytics(
        waterTrackingStatisticResolver = waterTrackingStatisticResolver,
        chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
        statisticLoadingController = viewModel.statisticLoadingController,
        onGoBack = navController::popBackStack,
    )
}