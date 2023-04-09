@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTag
import hardcoder.dev.presentation.dashboard.features.diary.DiaryViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.DropdownConfig
import hardcoder.dev.uikit.DropdownItem
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.FilledTextField

@Composable
fun DiaryScreen(
    onGoBack: () -> Unit,
    onCreateTrack: () -> Unit,
    onUpdateTrack: (Int) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getDiaryViewModel() }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        onFabClick = onCreateTrack,
        content = {
            DiaryContent(
                state = state.value,
                onUpdateTrack = onUpdateTrack,
                onSearchTrack = viewModel::updateSearchText,
                onAddFilterFeatureTag = viewModel::addFilterFeatureTag,
                onRemoveFilterFeatureTag = viewModel::removeFilterFeatureTag
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_title_topBar,
                onGoBack = onGoBack
            )
        ),
        dropdownConfig = DropdownConfig(
            actionToggle = Action(
                iconResId = R.drawable.ic_sort,
                onActionClick = {}
            ),
            dropdownItems = listOf(
                DropdownItem(
                    name = stringResource(id = R.string.diary_dropDownItem_filterByDay),
                    onDropdownItemClick = {
                        viewModel.updateSelectedFilterType(DateRangeFilterType.ByDay)
                    }
                ),
                DropdownItem(
                    name = stringResource(id = R.string.diary_dropDownItem_filterByWeek),
                    onDropdownItemClick = {
                        viewModel.updateSelectedFilterType(DateRangeFilterType.ByWeek)
                    }
                ),
                DropdownItem(
                    name = stringResource(id = R.string.diary_dropDownItem_filterByMonth),
                    onDropdownItemClick = {
                        viewModel.updateSelectedFilterType(DateRangeFilterType.ByMonth)
                    }
                ),
                DropdownItem(
                    name = stringResource(id = R.string.diary_dropDownItem_filterByYear),
                    onDropdownItemClick = {
                        viewModel.updateSelectedFilterType(DateRangeFilterType.ByYear)
                    }
                ),
                DropdownItem(
                    name = stringResource(id = R.string.diary_dropDownItem_filterByAllTime),
                    onDropdownItemClick = {
                        viewModel.updateSelectedFilterType(DateRangeFilterType.ByAllPeriod)
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
    onSearchTrack: (String) -> Unit,
    onAddFilterFeatureTag: (FeatureTag) -> Unit,
    onRemoveFilterFeatureTag: (FeatureTag) -> Unit
) {
    val inputService = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        FilledTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.searchText,
            onValueChange = onSearchTrack,
            label = R.string.diary_searchTrack_textField,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            leadingIcon = {
                Icon(
                    iconResId = R.drawable.ic_search,
                    contentDescription = stringResource(
                        id = R.string.diary_searchIcon_contentDescription
                    )
                )
            },
            trailingIcon = {
                Icon(
                    iconResId = R.drawable.ic_clear,
                    modifier = Modifier.clickable {
                        onSearchTrack("")
                        inputService?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilterFeatureTypeSection(
            state = state,
            onAddFilterFeatureTag = onAddFilterFeatureTag,
            onRemoveFilterFeatureTag = onRemoveFilterFeatureTag
        )
        if (state.diaryTrackList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                val items = if (state.searchText.isEmpty() && state.selectedFilterList.isEmpty()) {
                    state.diaryTrackList
                } else {
                    state.filteredTrackList
                }
                items(items) { diaryWithFeatureTags ->
                    DiaryItem(
                        diaryWithFeatureTags = diaryWithFeatureTags,
                        onUpdate = { onUpdateTrack(it.id) }
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            EmptySection(emptyTitleResId = R.string.diary_nowEmpty_text)
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