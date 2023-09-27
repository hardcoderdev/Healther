package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.diary.DateRangeFilterTypeResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.diary.Diary
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class DiaryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DiaryViewModel>()
        val dateRangeFilterTypeResourcesProvider = koinInject<DateRangeFilterTypeResourcesProvider>()
        val dateTimeFormatter = koinInject<DateTimeFormatter>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
        val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()

        Diary(
            dateTimeFormatter = dateTimeFormatter,
            millisDistanceFormatter = millisDistanceFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            dateRangeFilterTypeResourcesProvider = dateRangeFilterTypeResourcesProvider,
            dateRangeFilterTypeSelectionController = viewModel.dateRangeFilterTypeSelectionController,
            diaryTrackLoadingController = viewModel.diaryTrackLoadingController,
            filteredTrackLoadingController = viewModel.filteredTrackLoadingController,
            tagMultiSelectionController = viewModel.tagMultiSelectionController,
            rewardLoadingController = viewModel.rewardLoadingController,
            collectRewardController = viewModel.collectRewardController,
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