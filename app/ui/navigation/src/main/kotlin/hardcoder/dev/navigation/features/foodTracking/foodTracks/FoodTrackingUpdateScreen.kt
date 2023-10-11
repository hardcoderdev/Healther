package hardcoder.dev.navigation.features.foodTracking.foodTracks

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
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingUpdateViewModel
import hardcoder.dev.screens.features.foodTracking.foodTracks.FoodTrackUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class FoodTrackingUpdateScreen(val foodTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTrackingUpdateViewModel> {
            parametersOf(foodTrackId)
        }

        val dateTimeProvider = koinInject<DateTimeProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.deletionController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
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

        FoodTrackUpdate(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            dateInputController = viewModel.dateInputController,
            timeInputController = viewModel.timeInputController,
            foodSelectionController = viewModel.foodSelectionController,
            caloriesInputController = viewModel.caloriesInputController,
            updateController = viewModel.updateController,
            onGoBack = navigator::pop,
            onManageFoodTypes = {
                navigator += DrinkTypesScreen()
            },
            onDeleteDialogShow = {
                dialogOpen = it
            },
        )
    }
}