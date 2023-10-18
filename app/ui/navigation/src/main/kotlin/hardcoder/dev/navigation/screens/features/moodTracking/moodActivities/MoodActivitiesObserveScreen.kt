package hardcoder.dev.navigation.screens.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivitiesViewModel
import hardcoder.dev.screens.features.moodTracking.activity.MoodActivities
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoodActivitiesObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<MoodActivitiesViewModel>()

    MoodActivities(
        activitiesLoadingController = viewModel.activitiesLoadingController,
        onGoBack = navController::popBackStack,
        onCreateMoodActivity = {
            navController.navigate(Screen.MoodActivitiesCreate.route)
        },
        onUpdateMoodActivity = { moodActivityId ->
            navController.navigate(Screen.MoodActivitiesUpdate.buildRoute(moodActivityId))
        },
    )
}