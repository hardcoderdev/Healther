package hardcoder.dev.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.resources.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.screens.features.diary.observe.Diary
import org.koin.compose.koinInject

class DiaryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<DiaryViewModel>()
        val dateRangeFilterTypeResourcesProvider = koinInject<DateRangeFilterTypeResourcesProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()

        Diary(
            dateTimeFormatter = dateTimeFormatter,
            dateRangeFilterTypeResourcesProvider = dateRangeFilterTypeResourcesProvider,
            dateRangeFilterTypeSelectionController = viewModel.dateRangeFilterTypeSelectionController,
            diaryTrackLoadingController = viewModel.diaryTrackLoadingController,
            filteredTrackLoadingController = viewModel.filteredTrackLoadingController,
            tagMultiSelectionController = viewModel.tagMultiSelectionController,
            searchTextInputController = viewModel.searchTextInputController,
            onGoBack = navigator::pop,
            onCreateDiaryTrack = {
                navigator += DiaryCreationScreen()
            },
            onUpdateDiaryTrack = { diaryTrackId ->
                navigator += DiaryUpdateScreen(diaryTrackId)
            },
        )
    }
}