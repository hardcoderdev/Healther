package hardcoder.dev.navigation.screens.features.diary.diaryTags

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.diary.tags.DiaryTagUpdateViewModel
import hardcoder.dev.screens.features.diary.tags.update.DiaryTagUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun DiaryTagsUpdateScreen(
    diaryTagId: Int,
    navController: NavController,
) {
    val viewModel = koinViewModel<DiaryTagUpdateViewModel> {
        parametersOf(diaryTagId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.deleteController,
        action = navController::popBackStack,
    )

    DiaryTagUpdate(
        tagNameInputController = viewModel.tagNameInputController,
        iconSelectionController = viewModel.iconSelectionController,
        updateController = viewModel.updateController,
        onGoBack = navController::popBackStack,
    )
}