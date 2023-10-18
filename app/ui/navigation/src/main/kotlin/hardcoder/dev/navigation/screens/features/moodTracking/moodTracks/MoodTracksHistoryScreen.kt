package hardcoder.dev.navigation.screens.features.moodTracking.moodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.screens.features.moodTracking.history.MoodTrackingHistory
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun MoodTracksHistoryScreen(navController: NavController) {
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<MoodTrackingHistoryViewModel>()

    MoodTrackingHistory(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        dateRangeInputController = viewModel.dateRangeInputController,
        moodWithActivitiesLoadingController = viewModel.moodWithActivityLoadingController,
        onGoBack = navController::popBackStack,
    )
}