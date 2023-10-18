package hardcoder.dev.navigation.screens.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeUpdateViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.FoodTypeUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun FoodTypesUpdateScreen(
    foodTypeId: Int,
    navController: NavController,
) {
    val viewModel = koinViewModel<FoodTypeUpdateViewModel> {
        parametersOf(foodTypeId)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        controller = viewModel.deletionController,
        onUpdateDialogOpen = { dialogOpen = it },
    )

    LaunchedEffectWhenExecuted(
        controller = viewModel.deletionController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )

    FoodTypeUpdate(
        nameInputController = viewModel.nameInputController,
        iconSelectionController = viewModel.iconSelectionController,
        updateController = viewModel.updateController,
        onGoBack = navController::popBackStack,
        onDeleteDialogShow = {
            dialogOpen = it
        },
    )
}