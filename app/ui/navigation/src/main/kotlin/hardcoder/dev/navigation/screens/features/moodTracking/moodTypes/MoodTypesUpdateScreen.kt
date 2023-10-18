package hardcoder.dev.navigation.screens.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.update.MoodTypeUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MoodTypesUpdateScreen(
    moodTypeId: Int,
    navController: NavController,
) {
    val viewModel = koinViewModel<MoodTypeUpdateViewModel> {
        parametersOf(moodTypeId)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        controller = viewModel.deleteController,
        onUpdateDialogOpen = { dialogOpen = it },
    )

    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.deleteController,
        action = navController::popBackStack,
    )

    MoodTypeUpdate(
        moodTypeNameController = viewModel.moodTypeNameController,
        iconSelectionController = viewModel.iconSelectionController,
        positiveIndexController = viewModel.positiveIndexController,
        updateController = viewModel.updateController,
        onGoBack = navController::popBackStack,
        onDeleteDialog = {
            dialogOpen = it
        },
    )
}