package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.history.MoodTrackingHistory

class MoodTrackingHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodTrackingHistory(
            onGoBack = navigator::pop,
            onMoodTrackingUpdate = { moodTrackId -> // TODO MAYBE REMOVE UPDATE ?
                navigator += MoodTrackingUpdateScreen(moodTrackId)
            }
        )
    }
}