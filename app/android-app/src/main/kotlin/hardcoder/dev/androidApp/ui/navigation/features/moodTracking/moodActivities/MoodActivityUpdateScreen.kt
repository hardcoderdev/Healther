package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.update.MoodActivityUpdate

data class MoodActivityUpdateScreen(val moodActivityId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodActivityUpdate(
            activityId = moodActivityId,
            onGoBack = navigator::pop
        )
    }
}