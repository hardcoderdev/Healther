package hardcoder.dev.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.features.waterTracking.drinkTypes.DrinkTypesScreen
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.screens.features.waterTracking.waterTrack.update.WaterTrackingUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class WaterTrackingUpdateScreen(val waterTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<WaterTrackingUpdateViewModel> {
            parametersOf(waterTrackId)
        }
        val dateTimeProvider = koinInject<DateTimeProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.deletionController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.updatingController,
            action = navigator::pop,
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
            onGoBack = navigator::pop,
            onManageDrinkTypes = {
                navigator += DrinkTypesScreen()
            },
            onDeleteDialogShow = {
                dialogOpen = it
            },
        )
    }
}