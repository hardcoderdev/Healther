package hardcoder.dev.navigation.features.waterTracking.drinkTypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.screens.features.waterTracking.drinkType.update.DrinkTypeUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.core.parameter.parametersOf

data class DrinkTypeUpdateScreen(val drinkTypeId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DrinkTypeUpdateViewModel> {
            parametersOf(drinkTypeId)
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
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )

        DrinkTypeUpdate(
            nameInputController = viewModel.nameInputController,
            iconSelectionController = viewModel.iconSelectionController,
            waterPercentageInputController = viewModel.waterPercentageInputController,
            updateController = viewModel.updateController,
            onGoBack = navigator::pop,
            onDeleteDialogShow = {
                dialogOpen = it
            },
        )
    }
}