package hardcoder.dev.androidApp.ui.navigation.features.waterTracking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.drinkTypes.DrinkTypesScreen
import hardcoder.dev.androidApp.ui.screens.dialogs.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.update.WaterTrackingUpdate
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class WaterTrackingUpdateScreen(val waterTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<WaterTrackingUpdateViewModel> {
            parametersOf(waterTrackId)
        }

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
            viewModel = viewModel,
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