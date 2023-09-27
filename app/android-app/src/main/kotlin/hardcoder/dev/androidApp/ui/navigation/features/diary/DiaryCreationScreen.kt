package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.diary.tags.DiaryTagsScreen
import hardcoder.dev.androidApp.ui.screens.features.diary.create.DiaryCreation
import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

class DiaryCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DiaryCreationViewModel>()

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