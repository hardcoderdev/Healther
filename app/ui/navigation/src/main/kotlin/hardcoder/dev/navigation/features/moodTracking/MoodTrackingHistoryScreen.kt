package hardcoder.dev.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.screens.features.moodTracking.history.MoodTrackingHistory
import org.koin.compose.koinInject

class MoodTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTrackingHistoryViewModel>()
        val dateTimeProvider = koinInject<DateTimeProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        MoodTrackingHistory(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            dateRangeInputController = viewModel.dateRangeInputController,
            moodWithActivitiesLoadingController = viewModel.moodWithActivityLoadingController,
            onGoBack = navigator::pop,
        )
    }
}