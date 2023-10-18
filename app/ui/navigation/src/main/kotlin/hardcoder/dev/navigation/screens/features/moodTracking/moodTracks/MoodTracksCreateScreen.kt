package hardcoder.dev.navigation.screens.features.moodTracking.moodTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.screens.features.moodTracking.create.MoodTrackingCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun MoodTracksCreateScreen(navController: NavController) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val viewModel = koinViewModel<MoodTrackingCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    MoodTrackingCreation(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        noteInputController = viewModel.noteInputController,
        moodTypeSelectionController = viewModel.moodTypeSelectionController,
        activitiesMultiSelectionController = viewModel.activitiesMultiSelectionController,
        dateInputController = viewModel.dateController,
        timeInputController = viewModel.timeInputController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
        onManageMoodTypes = {
            navController.navigate(Screen.MoodTypesObserve.route)
        },
        onManageMoodActivities = {
            navController.navigate(Screen.MoodActivitiesObserve.route)
        },
    )
}