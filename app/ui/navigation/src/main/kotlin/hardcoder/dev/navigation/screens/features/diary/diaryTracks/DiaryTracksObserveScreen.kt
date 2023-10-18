package hardcoder.dev.navigation.screens.features.diary.diaryTracks

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.navigation.routes.Screen
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.resources.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.screens.features.diary.Diary
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun DiaryTracksObserveScreen(navController: NavController) {
    val dateRangeFilterTypeResourcesProvider = koinInject<DateRangeFilterTypeResourcesProvider>()
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val viewModel = koinViewModel<DiaryViewModel>()

    Diary(
        dateTimeFormatter = dateTimeFormatter,
        dateRangeFilterTypeResourcesProvider = dateRangeFilterTypeResourcesProvider,
        dateRangeFilterTypeSelectionController = viewModel.dateRangeFilterTypeSelectionController,
        diaryTrackLoadingController = viewModel.diaryTrackLoadingController,
        filteredTrackLoadingController = viewModel.filteredTrackLoadingController,
        tagMultiSelectionController = viewModel.tagMultiSelectionController,
        searchTextInputController = viewModel.searchTextInputController,
        onGoBack = navController::popBackStack,
        onCreateDiaryTrack = {
            navController.navigate(Screen.DiaryTracksCreate.route)
        },
        onUpdateDiaryTrack = { diaryTrackId ->
            navController.navigate(Screen.DiaryTracksUpdate.buildRoute(diaryTrackId))
        },
    )
}