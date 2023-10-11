package hardcoder.dev.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityCreationViewModel
import hardcoder.dev.screens.features.moodTracking.activity.create.MoodActivityCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class MoodActivityCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodActivityCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        MoodActivityCreation(
            activityNameController = viewModel.activityNameController,
            iconSingleSelectionController = viewModel.iconSelectionController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}