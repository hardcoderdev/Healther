package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.history.MoodTrackingHistory
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import org.koin.androidx.compose.koinViewModel

class MoodTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodTrackingHistoryViewModel>()

        MoodTrackingHistory(
            viewModel = viewModel,
            onGoBack = navigator::pop,
        )
    }
}