package hardcoder.dev.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.screens.features.moodTracking.MoodTracking
import org.koin.compose.koinInject

class MoodTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTrackingViewModel>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        MoodTracking(
            dateTimeFormatter = dateTimeFormatter,
            moodWithActivitiesController = viewModel.moodWithActivityLoadingController,
            onGoBack = navigator::pop,
            onCreateMoodTrack = {
                navigator += MoodTrackingCreationScreen()
            },
            onUpdateMoodTrack = { moodTrackId ->
                navigator += MoodTrackingUpdateScreen(moodTrackId)
            },
            onGoToHistory = {
                navigator += MoodTrackingHistoryScreen()
            },
            onGoToAnalytics = {
                navigator += MoodTrackingAnalyticsScreen()
            },
        )
    }
}