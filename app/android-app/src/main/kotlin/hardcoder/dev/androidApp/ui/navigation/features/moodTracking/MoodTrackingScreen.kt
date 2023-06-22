package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.MoodTracking

class MoodTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodTracking(
            onGoBack = navigator::pop,
            onCreateMoodTrack = {
                navigator += MoodTrackingCreationScreen()
            },
            onUpdateMoodTrack = { moodTrack ->
                navigator += MoodTrackingUpdateScreen(moodTrack.id)
            },
            onGoToHistory = {
                navigator += MoodTrackingHistoryScreen()
            }
        )
    }
}