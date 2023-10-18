package hardcoder.dev.navigation.screens.features.diary.diaryTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.screens.features.diary.update.DiaryUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
internal fun DiaryTracksUpdateScreen(
    diaryTrackId: Int,
    navController: NavController,
) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<DiaryUpdateViewModel> {
        parametersOf(diaryTrackId)
    }

    LaunchedEffectWhenExecuted(
        controller = viewModel.updateController,
        action = navController::popBackStack,
    )
    LaunchedEffectWhenExecuted(
        controller = viewModel.deleteController,
        action = navController::popBackStack,
    )

    DiaryUpdate(
        dateTimeFormatter = dateTimeFormatter,
        contentInputController = viewModel.contentInputController,
        tagMultiSelectionController = viewModel.tagMultiSelectionController,
        updateController = viewModel.updateController,
        deleteController = viewModel.deleteController,
        diaryAttachmentsLoadingController = viewModel.diaryAttachmentsLoadingController,
        onGoBack = navController::popBackStack,
        onManageTags = {
            navController.navigate(Screen.DiaryTagsObserve.route)
        },
    )
}