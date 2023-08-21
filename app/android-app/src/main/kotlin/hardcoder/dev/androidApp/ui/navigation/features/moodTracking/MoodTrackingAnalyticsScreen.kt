package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.analytics.MoodTrackingAnalytics
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingAnalyticsViewModel
import org.koin.compose.koinInject

class MoodTrackingAnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinInject<MoodTrackingAnalyticsViewModel>()

        MoodTrackingAnalytics(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}