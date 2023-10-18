package hardcoder.dev.navigation.screens.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityUpdateViewModel
import hardcoder.dev.screens.features.moodTracking.activity.update.MoodActivityUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MoodActivitiesUpdateScreen(
    moodActivityId: Int,
    navController: NavController,
) {
    val viewModel = koinViewModel<MoodActivityUpdateViewModel> {
        parametersOf(moodActivityId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.deleteController,
        action = navController::popBackStack,
    )

    MoodActivityUpdate(
        activityNameController = viewModel.activityNameController,
        iconSingleSelectionController = viewModel.iconSingleSelectionController,
        updateController = viewModel.updateController,
        deleteController = viewModel.deleteController,
        onGoBack = navController::popBackStack,
    )
}