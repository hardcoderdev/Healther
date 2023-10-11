package hardcoder.dev.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import hardcoder.dev.resolvers.features.waterTracking.WaterTrackingStatisticResolver
import hardcoder.dev.screens.features.waterTracking.waterTrack.analytics.WaterTrackingAnalytics
import org.koin.compose.koinInject

class WaterTrackingAnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WaterTrackingAnalyticsViewModel>()
        val waterTrackingStatisticResolver = koinInject<WaterTrackingStatisticResolver>()

        WaterTrackingAnalytics(
            waterTrackingStatisticResolver = waterTrackingStatisticResolver,
            chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
            statisticLoadingController = viewModel.statisticLoadingController,
            onGoBack = navigator::pop,
        )
    }
}