package hardcoder.dev.navigation.screens.features.diary.diaryTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.screens.features.diary.create.DiaryCreation
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DiaryTracksCreateScreen(navController: NavController) {
    val viewModel = koinViewModel<DiaryCreationViewModel>()

    LaunchedEffectWhenExecuted(
        controller = viewModel.creationController,
        action = navController::popBackStack,
    )

    DiaryCreation(
        contentController = viewModel.contentController,
        creationController = viewModel.creationController,
        tagMultiSelectionController = viewModel.tagMultiSelectionController,
        onGoBack = navController::popBackStack,
        onManageTags = {
            navController.navigate(Screen.DiaryTagsObserve.route)
        },
    )
}