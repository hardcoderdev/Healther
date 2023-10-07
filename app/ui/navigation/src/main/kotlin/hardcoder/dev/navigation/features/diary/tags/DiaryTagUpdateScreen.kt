package hardcoder.dev.androidApp.ui.navigation.features.diary.tags

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.diary.tags.DiaryTagUpdateViewModel
import hardcoder.dev.screens.features.diary.tags.update.DiaryTagUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class DiaryTagUpdateScreen(val diaryTagId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DiaryTagUpdateViewModel> {
            parametersOf(diaryTagId)
        }

        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.deleteController,
            action = navigator::pop,
        )

        DiaryTagUpdate(
            tagNameInputController = viewModel.tagNameInputController,
            iconSelectionController = viewModel.iconSelectionController,
            updateController = viewModel.updateController,
            onGoBack = navigator::pop,
        )
    }
}