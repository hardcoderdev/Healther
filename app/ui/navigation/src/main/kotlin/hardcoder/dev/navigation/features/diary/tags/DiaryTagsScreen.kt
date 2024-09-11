package hardcoder.dev.navigation.features.diary.tags

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.diary.tags.DiaryTagsViewModel
import hardcoder.dev.screens.features.diary.tags.observe.DiaryTags

class DiaryTagsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DiaryTagsViewModel>()

        DiaryTags(
            diaryTagsLoadingController = viewModel.diaryTagsLoadingController,
            onGoBack = navigator::pop,
            onCreateDiaryTag = {
                navigator += DiaryTagCreationScreen()
            },
            onUpdateDiaryTag = { diaryTagId ->
                navigator += DiaryTagUpdateScreen(diaryTagId)
            },
        )
    }
}