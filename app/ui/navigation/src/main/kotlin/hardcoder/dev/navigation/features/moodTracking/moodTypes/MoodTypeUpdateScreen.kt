package hardcoder.dev.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.update.MoodTypeUpdate
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.core.parameter.parametersOf

data class MoodTypeUpdateScreen(val moodTypeId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTypeUpdateViewModel> {
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
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.deleteController,
            action = navigator::pop,
        )

        MoodTypeUpdate(
            moodTypeNameController = viewModel.moodTypeNameController,
            iconSelectionController = viewModel.iconSelectionController,
            positiveIndexController = viewModel.positiveIndexController,
            updateController = viewModel.updateController,
            onGoBack = navigator::pop,
            onDeleteDialog = {
                dialogOpen = it
            },
        )
    }
}