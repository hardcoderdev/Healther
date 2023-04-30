@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.dashboard.diary.items.DiaryFastingItem
import hardcoder.dev.androidApp.ui.dashboard.diary.items.DiaryItem
import hardcoder.dev.androidApp.ui.dashboard.diary.items.DiaryMoodItem
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.presentation.dashboard.features.diary.DiaryViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.BottomSheet
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.rememberBottomSheetState
import hardcoder.dev.uikit.sections.EmptyBlock
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import kotlinx.coroutines.launch

@Composable
fun DiaryScreen(
    onGoBack: () -> Unit,
    onCreateTrack: () -> Unit,
    onUpdateTrack: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getDiaryViewModel() }
    val state = viewModel.state.collectAsState()
    val filterBottomSheetState = rememberBottomSheetState(showed = false)

    BottomSheet(
        sheetContent = {
            FilterBottomSheetContent(
                state = state.value,
                onUpdateDateRangeFilterType = viewModel::updateSelectedFilterType,
                onToggleFilterTag = viewModel::toggleFilterTag,
                onClose = {
                    scope.launch {
                        filterBottomSheetState.toggle()
                    }
                }
            )
        },
        state = filterBottomSheetState
    ) {
        ScaffoldWrapper(
            onFabClick = onCreateTrack,
            content = {
                DiaryContent(
                    state = state.value,
                    onUpdateTrack = onUpdateTrack
                )
            },
            topBarConfig = TopBarConfig(
                type = TopBarType.SearchTopBar(
                    titleResId = R.string.diary_title_topBar,
                    searchText = state.value.searchText,
                    placeholderText = R.string.diary_searchTrack_textField,
                    onGoBack = onGoBack,
                    onSearchTextChanged = viewModel::updateSearchText,
                    onClearClick = { viewModel.updateSearchText("") }
                )
            ),
            actionConfig = ActionConfig(
                listOf(
                    Action(
                        iconResId = R.drawable.ic_sort,
                        onActionClick = {
                            scope.launch {
                                filterBottomSheetState.toggle()
                            }
                        }
                    )
                )
            )
        )
    }
}

@Composable
private fun DiaryContent(
    state: DiaryViewModel.State,
    onUpdateTrack: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when {
            state.diaryTrackList.isEmpty() -> {
                Spacer(modifier = Modifier.height(16.dp))
                EmptySection(emptyTitleResId = R.string.diary_nowEmpty_text)
            }

            state.searchText.isEmpty() && state.selectedTagList.isEmpty() -> {
                DiaryTrackListSection(items = state.diaryTrackList, onUpdateTrack = onUpdateTrack)
            }

            state.filteredTrackList.isEmpty() -> {
                Spacer(modifier = Modifier.height(16.dp))
                EmptyBlock(
                    lottieAnimationResId = R.raw.empty_astronaut,
                    modifier = Modifier.height(100.dp),
                    emptyTitleResId = R.string.diary_nothingFound_text
                )
            }

            else -> {
                DiaryTrackListSection(
                    items = state.filteredTrackList,
                    onUpdateTrack = onUpdateTrack
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.DiaryTrackListSection(
    items: List<DiaryTrack>,
    onUpdateTrack: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items) { diaryTrack ->
            DiaryItem(
                diaryTrack = diaryTrack,
                onUpdate = { onUpdateTrack(it.id) },
            )
        }
    }
}

@Composable
private fun FilterBottomSheetContent(
    modifier: Modifier = Modifier,
    state: DiaryViewModel.State,
    onClose: () -> Unit,
    onUpdateDateRangeFilterType: (DateRangeFilterType) -> Unit,
    onToggleFilterTag: (DiaryTag) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Title(
                text = stringResource(R.string.diary_filterBottomSheetTitle_text),
                modifier = Modifier.weight(2f)
            )
            Icon(iconResId = R.drawable.ic_clear, modifier = Modifier.clickable { onClose() })
        }
        Spacer(modifier = Modifier.height(32.dp))
        DateRangeSection(state = state, onUpdateDateRangeFilterType = onUpdateDateRangeFilterType)
        Spacer(modifier = Modifier.height(32.dp))
        FilterTagSection(
            state = state,
            onToggleFilterTag = onToggleFilterTag
        )
    }
}

@Composable
private fun DateRangeSection(
    state: DiaryViewModel.State,
    onUpdateDateRangeFilterType: (DateRangeFilterType) -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateRangeFilterTypeResourcesProvider = uiModule.dateRangeFilterTypeResourcesProvider

    Description(text = stringResource(R.string.diary_selectDateRange_subtitle_bottomSheet_text))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        maxItemsInEachRow = 4
    ) {
        state.dateRangeFilterTypes.forEach { dateRangeFilter ->
            val dateRangeFilterTypeResources =
                dateRangeFilterTypeResourcesProvider.provide(dateRangeFilter)

            Chip(
                interactionType = InteractionType.SELECTION,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp, top = 16.dp),
                isSelected = state.selectedDateRangeFilterType == dateRangeFilter,
                text = stringResource(id = dateRangeFilterTypeResources.nameResId),
                onClick = {
                    onUpdateDateRangeFilterType(dateRangeFilter)
                }
            )
        }
    }
}

@Composable
private fun FilterTagSection(
    state: DiaryViewModel.State,
    onToggleFilterTag: (DiaryTag) -> Unit
) {
    Description(text = stringResource(R.string.diary_selectTags_subtitle_bottomSheet_text))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        maxItemsInEachRow = 8
    ) {
        state.tagList.forEach { diaryTag ->
            Chip(
                interactionType = InteractionType.SELECTION,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp, top = 16.dp),
                isSelected = state.selectedTagList.contains(diaryTag),
                text = diaryTag.name,
                onClick = {
                    onToggleFilterTag(diaryTag)
                    Log.d("ddlepdle", "${state.selectedTagList.contains(diaryTag)}")
                }
            )
        }
    }
}