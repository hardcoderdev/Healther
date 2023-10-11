package hardcoder.dev.navigation.features.foodTracking.foodTypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeUpdateViewModel
import hardcoder.dev.screens.features.foodTracking.foodTypes.FoodTypeUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.core.parameter.parametersOf

data class FoodTypeUpdateScreen(val foodTypeId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FoodTypeUpdateViewModel> {
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
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )

        FoodTypeUpdate(
            nameInputController = viewModel.nameInputController,
            iconSelectionController = viewModel.iconSelectionController,
            updateController = viewModel.updateController,
            onGoBack = navigator::pop,
            onDeleteDialogShow = {
                dialogOpen = it
            },
        )
    }
}