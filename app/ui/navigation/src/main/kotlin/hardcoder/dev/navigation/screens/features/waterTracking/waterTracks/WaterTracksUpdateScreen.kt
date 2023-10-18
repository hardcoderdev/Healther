package hardcoder.dev.navigation.screens.features.waterTracking.waterTracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.update.WaterTrackingUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun WaterTracksUpdateScreen(
    waterTrackId: Int,
    navController: NavController,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<WaterTrackingUpdateViewModel> {
        parametersOf(waterTrackId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.deletionController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.updatingController,
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

    WaterTrackingUpdate(
        dateTimeProvider = dateTimeProvider,
        dateTimeFormatter = dateTimeFormatter,
        dateInputController = viewModel.dateInputController,
        timeInputController = viewModel.timeInputController,
        drinkSelectionController = viewModel.drinkSelectionController,
        millilitersDrunkInputController = viewModel.millilitersDrunkInputController,
        updateController = viewModel.updatingController,
        onGoBack = navController::popBackStack,
        onManageDrinkTypes = {
            navController.navigate(Screen.DrinkTypesObserve.route)
        },
        onDeleteDialogShow = {
            dialogOpen = it
        },
    )
}