package hardcoder.dev.navigation.screens.features.diary.diaryTags

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.diary.tags.DiaryTagsViewModel
import hardcoder.dev.screens.features.diary.tags.DiaryTags
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DiaryTagsObserveScreen(navController: NavController) {
    val viewModel = koinViewModel<DiaryTagsViewModel>()

    DiaryTags(
        diaryTagsLoadingController = viewModel.diaryTagsLoadingController,
        onGoBack = navController::popBackStack,
        onCreateDiaryTag = {
            navController.navigate(Screen.DiaryTagsCreate.route)
        },
        onUpdateDiaryTag = { diaryTagId ->
            navController.navigate(Screen.DiaryTagsUpdate.buildRoute(diaryTagId))
        },
    )
}