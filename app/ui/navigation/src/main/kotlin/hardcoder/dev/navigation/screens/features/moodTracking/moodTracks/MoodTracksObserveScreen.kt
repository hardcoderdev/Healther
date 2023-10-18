package hardcoder.dev.navigation.screens.features.moodTracking.moodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.screens.features.moodTracking.MoodTracking
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun MoodTracksObserveScreen(navController: NavController) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<MoodTrackingViewModel>()

    MoodTracking(
        dateTimeFormatter = dateTimeFormatter,
        moodWithActivitiesController = viewModel.moodWithActivityLoadingController,
        onGoBack = navController::popBackStack,
        onCreateMoodTrack = {
            navController.navigate(Screen.MoodTracksCreate.route)
        },
        onUpdateMoodTrack = { moodTrackId ->
            navController.navigate(Screen.MoodTracksUpdate.buildRoute(moodTrackId))
        },
        onGoToHistory = {
            navController.navigate(Screen.MoodTracksHistory.route)
        },
        onGoToAnalytics = {
            navController.navigate(Screen.MoodTracksAnalytics.route)
        },
    )
}