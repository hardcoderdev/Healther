package hardcoder.dev.androidApp.ui.features.diary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import hardcoder.dev.androidApp.ui.features.diary.items.DiaryItem
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItems
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.diary.DateRangeFilterType
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.BottomSheet
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.lists.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.lists.flowRow.SingleSelectionChipFlowRow
import hardcoder.dev.uikit.rememberBottomSheetState
import hardcoder.dev.uikit.sections.EmptyBlock
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Label
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
    val filterBottomSheetState = rememberBottomSheetState(showed = false)

    BottomSheet(
        state = filterBottomSheetState,
        sheetContent = {
            FilterBottomSheetContent(
                dateRangeFilterTypeSelectionController = viewModel.dateRangeFilterTypeSelectionController,
                tagMultiSelectionController = viewModel.tagMultiSelectionController,
                onClose = {
                    scope.launch {
                        filterBottomSheetState.toggle()
                    }
                }
            )
        }
    ) {
        val searchText = viewModel.searchTextInputController.state.collectAsState()

        ScaffoldWrapper(
            onFabClick = onCreateTrack,
            content = {
                DiaryContent(
                    diaryTrackLoadingController = viewModel.diaryTrackLoadingController,
                    filteredTrackLoadingController = viewModel.filteredTrackLoadingController,
                    tagMultiSelectionController = viewModel.tagMultiSelectionController,
                    searchText = searchText.value.input,
                    onUpdateTrack = onUpdateTrack
                )
            },
            topBarConfig = TopBarConfig(
                type = TopBarType.SearchTopBarController(
                    controller = viewModel.searchTextInputController,
                    titleResId = R.string.diary_title_topBar,
                    placeholderText = R.string.diary_searchTrack_textField,
                    onGoBack = onGoBack
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
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    diaryTrackLoadingController: LoadingController<List<DiaryTrack>>,
    filteredTrackLoadingController: LoadingController<List<DiaryTrack>>,
    searchText: String,
    onUpdateTrack: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LoadingContainer(
            controller1 = diaryTrackLoadingController,
            controller2 = filteredTrackLoadingController,
            loadedContent = { diaryTrackList, filteredDiaryTrackList ->
                when {
                    diaryTrackList.isEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        EmptySection(emptyTitleResId = R.string.diary_nowEmpty_text)
                    }

                    searchText.isEmpty() && tagMultiSelectionController.state.collectAsState().value is MultiSelectionController.State.Empty -> {
                        DiaryTrackListSection(items = diaryTrackList, onUpdateTrack = onUpdateTrack)
                    }

                    filteredDiaryTrackList.isEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        EmptyBlock(
                            lottieAnimationResId = R.raw.empty_astronaut,
                            modifier = Modifier.height(100.dp),
                            emptyTitleResId = R.string.diary_nothingFound_text
                        )
                    }

                    else -> {
                        DiaryTrackListSection(
                            items = filteredDiaryTrackList,
                            onUpdateTrack = onUpdateTrack
                        )
                    }
                }
            }
        )
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
    dateRangeFilterTypeSelectionController: SingleSelectionController<DateRangeFilterType>,
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    onClose: () -> Unit,
) {
    val state = tagMultiSelectionController.state.collectAsState().value

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
        DateRangeSection(dateRangeFilterTypeSelectionController = dateRangeFilterTypeSelectionController)
        if (state is MultiSelectionController.State.Loaded) {
            if (state.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                FilterTagSection(tagMultiSelectionController = tagMultiSelectionController)
            }
        }
    }
}

@Composable
private fun DateRangeSection(dateRangeFilterTypeSelectionController: SingleSelectionController<DateRangeFilterType>) {
    val uiModule = LocalUIModule.current
    val dateRangeFilterTypeResourcesProvider = uiModule.dateRangeFilterTypeResourcesProvider

    Description(text = stringResource(R.string.diary_selectDateRange_subtitle_bottomSheet_text))
    SingleSelectionChipFlowRow(
        controller = dateRangeFilterTypeSelectionController,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier
            .wrapContentSize()
            .padding(end = 8.dp, top = 16.dp),
        chipShape = RoundedCornerShape(16.dp),
        maxItemsInEachRow = 4,
        itemContent = { dateRangeFilter, _ ->
            val dateRangeFilterTypeResources =
                dateRangeFilterTypeResourcesProvider.provide(dateRangeFilter)
            Label(text = stringResource(id = dateRangeFilterTypeResources.nameResId))
        }
    )
}

@Composable
private fun FilterTagSection(tagMultiSelectionController: MultiSelectionController<DiaryTag>) {
    Description(text = stringResource(R.string.diary_selectTags_subtitle_bottomSheet_text))
    MultiSelectionChipFlowRow(
        controller = tagMultiSelectionController,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        itemModifier = Modifier
            .wrapContentSize()
            .padding(end = 8.dp, top = 16.dp),
        maxItemsInEachRow = 8,
        chipShape = RoundedCornerShape(16.dp),
        itemContent = { tag, _ ->
            ChipIconDefaultContent(
                iconResId = tag.icon.resourceId,
                name = tag.name
            )
        }
    )
}