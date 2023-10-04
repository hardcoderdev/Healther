package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities.MoodActivitiesScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes.MoodTypesScreen
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.update.MoodTrackingUpdate
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingUpdateViewModel
import hardcoder.dev.uikit.components.dialog.DeleteTrackDialog
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class MoodTrackingUpdateScreen(val moodTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodTrackingUpdateViewModel> {
            parametersOf(moodTrackId)
        }
        val dateTimeFormatter = koinInject<hardcoder.dev.formatters.DateTimeFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.deleteController,
            action = navigator::pop,
        )

        var dialogOpen by remember {
            mutableStateOf(false)
        }

        DeleteTrackDialog(
            dialogOpen = dialogOpen,
            controller = viewModel.deleteController,
            onUpdateDialogOpen = { dialogOpen = it },
        )

        MoodTrackingUpdate(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            dateInputController = viewModel.dateController,
            timeInputController = viewModel.timeInputController,
            noteInputController = viewModel.noteInputController,
            moodTypeSelectionController = viewModel.moodTypeSelectionController,
            updateController = viewModel.updateController,
            activitiesMultiSelectionController = viewModel.activitiesMultiSelectionController,
            onGoBack = navigator::pop,
            onManageMoodTypes = {
                navigator += MoodTypesScreen()
            },
            onManageMoodActivities = {
                navigator += MoodActivitiesScreen()
            },
            onDeleteShowDialog = {
                dialogOpen = it
            },
        )
    }
}