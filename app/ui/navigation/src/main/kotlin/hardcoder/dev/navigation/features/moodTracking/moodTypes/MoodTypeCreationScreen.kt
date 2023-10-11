package hardcoder.dev.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreationViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.create.MoodTypeCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class MoodTypeCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodTypeCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        MoodTypeCreation(
            moodTypeNameController = viewModel.moodTypeNameController,
            iconSelectionController = viewModel.iconSelectionController,
            positiveIndexController = viewModel.positiveIndexController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}