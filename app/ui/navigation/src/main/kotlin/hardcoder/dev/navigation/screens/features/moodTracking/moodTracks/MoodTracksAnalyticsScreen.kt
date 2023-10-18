package hardcoder.dev.navigation.screens.features.moodTracking.moodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingAnalyticsViewModel
import hardcoder.dev.resolvers.features.moodTracking.MoodTrackingStatisticResolver
import hardcoder.dev.screens.features.moodTracking.analytics.MoodTrackingAnalytics
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun MoodTracksAnalyticsScreen(navController: NavController) {
    val moodTrackingStatisticResolver = koinInject<MoodTrackingStatisticResolver>()
    val viewModel = koinViewModel<MoodTrackingAnalyticsViewModel>()

    MoodTrackingAnalytics(
        moodTrackingStatisticResolver = moodTrackingStatisticResolver,
        statisticLoadingController = viewModel.statisticLoadingController,
        chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
        onGoBack = navController::popBackStack,
    )
}