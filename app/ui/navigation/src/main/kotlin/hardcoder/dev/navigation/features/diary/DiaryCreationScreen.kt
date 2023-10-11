package hardcoder.dev.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.navigation.features.diary.tags.DiaryTagsScreen
import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.screens.features.diary.create.DiaryCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted

class DiaryCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DiaryCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        DiaryCreation(
            contentController = viewModel.contentController,
            creationController = viewModel.creationController,
            tagMultiSelectionController = viewModel.tagMultiSelectionController,
            onGoBack = navigator::pop,
            onManageTags = {
                navigator += DiaryTagsScreen()
            },
        )
    }
}