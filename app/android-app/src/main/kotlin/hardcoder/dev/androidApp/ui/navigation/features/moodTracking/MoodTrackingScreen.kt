package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.MoodTracking
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import org.koin.androidx.compose.koinViewModel

class MoodTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodTrackingViewModel>()

        MoodTracking(
            viewModel = viewModel,
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
        )
    }
}