@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTags
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTag
import hardcoder.dev.presentation.dashboard.features.diary.DiaryViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.sections.EmptyBlock
import hardcoder.dev.uikit.sections.EmptySection
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

    ScaffoldWrapper(
        onFabClick = onCreateTrack,
        content = {
            DiaryContent(
                state = state.value,
                onUpdateTrack = onUpdateTrack,
                onAddFilterFeatureTag = viewModel::addFilterFeatureTag,
                onRemoveFilterFeatureTag = viewModel::removeFilterFeatureTag
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
                            //filterBottomSheetState.expand()
                        }
                    }
                )
            )
        )
    )
}

@Composable
private fun DiaryContent(
    state: DiaryViewModel.State,
    onUpdateTrack: (Int) -> Unit,
    onAddFilterFeatureTag: (FeatureTag) -> Unit,
    onRemoveFilterFeatureTag: (FeatureTag) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        FilterFeatureTypeSection(
            state = state,
            onAddFilterFeatureTag = onAddFilterFeatureTag,
            onRemoveFilterFeatureTag = onRemoveFilterFeatureTag
        )
        when {
            state.diaryTrackList.isEmpty() -> {
                Spacer(modifier = Modifier.height(16.dp))
                EmptySection(emptyTitleResId = R.string.diary_nowEmpty_text)
            }

            state.searchText.isEmpty() && state.selectedFilterList.isEmpty() -> {
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
private fun FilterFeatureTypeSection(
    state: DiaryViewModel.State,
    onAddFilterFeatureTag: (FeatureTag) -> Unit,
    onRemoveFilterFeatureTag: (FeatureTag) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        maxItemsInEachRow = 8
    ) {
        state.filterList.forEach { filterFeatureTag ->
            Chip(
                interactionType = InteractionType.SELECTION,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp, top = 16.dp),
                isSelected = state.selectedFilterList.contains(filterFeatureTag),
                text = filterFeatureTag.name,
                onClick = {
                    if (state.selectedFilterList.contains(filterFeatureTag)) {
                        onRemoveFilterFeatureTag(filterFeatureTag)
                    } else {
                        onAddFilterFeatureTag(filterFeatureTag)
                    }
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.DiaryTrackListSection(
    items: List<DiaryWithFeatureTags>,
    onUpdateTrack: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items) { diaryWithFeatureTags ->
            DiaryItem(
                diaryWithFeatureTags = diaryWithFeatureTags,
                onUpdate = { onUpdateTrack(it.id) }
            )
        }
    }
}