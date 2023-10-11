package hardcoder.dev.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.features.diary.tags.DiaryTagsScreen
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.screens.features.diary.update.DiaryUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class DiaryUpdateScreen(val diaryTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DiaryUpdateViewModel> {
            parametersOf(diaryTrackId)
        }
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.deleteController,
            action = navigator::pop,
        )

        DiaryUpdate(
            dateTimeFormatter = dateTimeFormatter,
            contentInputController = viewModel.contentInputController,
            tagMultiSelectionController = viewModel.tagMultiSelectionController,
            updateController = viewModel.updateController,
            deleteController = viewModel.deleteController,
            diaryAttachmentsLoadingController = viewModel.diaryAttachmentsLoadingController,
            onGoBack = navigator::pop,
            onManageTags = {
                navigator += DiaryTagsScreen()
            },
        )
    }
}