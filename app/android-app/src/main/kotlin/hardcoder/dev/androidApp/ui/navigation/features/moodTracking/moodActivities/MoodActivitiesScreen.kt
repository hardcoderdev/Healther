package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.activity.MoodActivities
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivitiesViewModel
import org.koin.androidx.compose.koinViewModel

class MoodActivitiesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodActivitiesViewModel>()

        MoodActivities(
            activitiesLoadingController = viewModel.activitiesLoadingController,
            onGoBack = navigator::pop,
            onCreateMoodActivity = {
                navigator += MoodActivityCreationScreen()
            },
            onUpdateMoodActivity = { moodActivityId ->
                navigator += MoodActivityUpdateScreen(moodActivityId)
            },
        )
    }
}