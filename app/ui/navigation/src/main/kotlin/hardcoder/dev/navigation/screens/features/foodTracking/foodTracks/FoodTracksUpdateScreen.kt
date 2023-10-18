package hardcoder.dev.navigation.screens.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingUpdateViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.FoodTrackUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
internal fun FoodTracksUpdateScreen(
    foodTrackId: Int,
    navController: NavController,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<FoodTrackingUpdateViewModel> {
        parametersOf(foodTrackId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.deletionController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        controller = viewModel.deletionController,
        onUpdateDialogOpen = { dialogOpen = it },
    )

    FoodTrackUpdate(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        dateInputController = viewModel.dateInputController,
        timeInputController = viewModel.timeInputController,
        foodSelectionController = viewModel.foodSelectionController,
        caloriesInputController = viewModel.caloriesInputController,
        updateController = viewModel.updateController,
        onGoBack = navController::popBackStack,
        onManageFoodTypes = {
            navController.navigate(Screen.FoodTypesObserve.route)
        },
        onDeleteDialogShow = {
            dialogOpen = it
        },
    )
}