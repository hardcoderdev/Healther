package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.ManageMoodActivities

class ManageMoodActivitiesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageMoodActivities(
            onGoBack = navigator::pop,
            onCreateMoodActivity = {
                navigator += MoodActivityCreationScreen()
            },
            onUpdateMoodActivity = { moodActivityId ->
                navigator += MoodActivityUpdateScreen(moodActivityId)
            }
        )
    }
}