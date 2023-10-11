package hardcoder.dev.navigation.features.diary.tags

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.diary.tags.DiaryTagCreationViewModel
import hardcoder.dev.screens.features.diary.tags.create.DiaryTagCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class DiaryTagCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DiaryTagCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        DiaryTagCreation(
            tagNameInputController = viewModel.nameInputController,
            iconSelectionController = viewModel.iconSelectionController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}