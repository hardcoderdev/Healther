package hardcoder.dev.navigation.screens.features.diary.diaryTags

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.presentation.features.diary.tags.DiaryTagCreationViewModel
import hardcoder.dev.screens.features.diary.tags.create.DiaryTagCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DiaryTagsCreationScreen(navController: NavController) {
    val viewModel = koinViewModel<DiaryTagCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    DiaryTagCreation(
        tagNameInputController = viewModel.nameInputController,
        iconSelectionController = viewModel.iconSelectionController,
        creationController = viewModel.creationController,
        onGoBack = navController::popBackStack,
    )
}