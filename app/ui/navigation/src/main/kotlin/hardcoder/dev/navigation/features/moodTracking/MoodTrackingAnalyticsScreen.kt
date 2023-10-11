package hardcoder.dev.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingAnalyticsViewModel
import hardcoder.dev.resolvers.features.moodTracking.MoodTrackingStatisticResolver
import hardcoder.dev.screens.features.moodTracking.analytics.MoodTrackingAnalytics
import org.koin.compose.koinInject

class MoodTrackingAnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTrackingAnalyticsViewModel>()
        val moodTrackingStatisticResolver = koinInject<MoodTrackingStatisticResolver>()

        MoodTrackingAnalytics(
            moodTrackingStatisticResolver = moodTrackingStatisticResolver,
            statisticLoadingController = viewModel.statisticLoadingController,
            chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
            onGoBack = navigator::pop,
        )
    }
}