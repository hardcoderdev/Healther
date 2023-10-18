package hardcoder.dev.navigation.screens.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityCreationViewModel
import hardcoder.dev.screens.features.moodTracking.activity.create.MoodActivityCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoodActivitiesCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<MoodActivityCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    MoodActivityCreation(
        activityNameController = viewModel.activityNameController,
        iconSingleSelectionController = viewModel.iconSelectionController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
    )
}