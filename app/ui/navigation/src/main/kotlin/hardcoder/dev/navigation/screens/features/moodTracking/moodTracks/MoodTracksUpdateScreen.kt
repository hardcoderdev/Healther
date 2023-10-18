package hardcoder.dev.navigation.screens.features.moodTracking.moodTracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingUpdateViewModel
import hardcoder.dev.screens.features.moodTracking.update.MoodTrackingUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
internal fun MoodTracksUpdateScreen(
    moodTrackId: Int,
    navController: NavController,
) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val viewModel = koinViewModel<MoodTrackingUpdateViewModel> {
        parametersOf(moodTrackId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.deleteController,
        action = navController::popBackStack,
    )

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        controller = viewModel.deleteController,
        onUpdateDialogOpen = { dialogOpen = it },
    )

    MoodTrackingUpdate(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        dateInputController = viewModel.dateController,
        timeInputController = viewModel.timeInputController,
        noteInputController = viewModel.noteInputController,
        moodTypeSelectionController = viewModel.moodTypeSelectionController,
        updateController = viewModel.updateController,
        activitiesMultiSelectionController = viewModel.activitiesMultiSelectionController,
        onGoBack = navController::popBackStack,
        onManageMoodTypes = {
            navController.navigate(Screen.MoodTypesObserve.route)
        },
        onManageMoodActivities = {
            navController.navigate(Screen.MoodActivitiesObserve.route)
        },
        onDeleteShowDialog = {
            dialogOpen = it
        },
    )
}