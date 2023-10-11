package hardcoder.dev.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.features.moodTracking.moodActivities.MoodActivitiesScreen
import hardcoder.dev.navigation.features.moodTracking.moodTypes.MoodTypesScreen
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.screens.features.moodTracking.create.MoodTrackingCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject

class MoodTrackingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTrackingCreationViewModel>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val dateTimeProvider = koinInject<DateTimeProvider>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        MoodTrackingCreation(
            dateTimeProvider = dateTimeProvider,
            dateTimeFormatter = dateTimeFormatter,
            noteInputController = viewModel.noteInputController,
            moodTypeSelectionController = viewModel.moodTypeSelectionController,
            activitiesMultiSelectionController = viewModel.activitiesMultiSelectionController,
            dateInputController = viewModel.dateController,
            timeInputController = viewModel.timeInputController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
            onManageMoodTypes = {
                navigator += MoodTypesScreen()
            },
            onManageMoodActivities = {
                navigator += MoodActivitiesScreen()
            },
        )
    }
}