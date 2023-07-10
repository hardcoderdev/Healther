package hardcoder.dev.androidApp.ui.screens.features.diary

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
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.androidApp.ui.screens.features.diary.items.DiaryItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.features.diary.DateRangeFilterType
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.uikit.components.bottomSheet.BottomSheet
import hardcoder.dev.uikit.components.bottomSheet.rememberBottomSheetState
import hardcoder.dev.uikit.components.chip.content.ChipIconDefaultContent
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.list.flowRow.MultiSelectionChipFlowRow
import hardcoder.dev.uikit.components.list.flowRow.SingleSelectionChipFlowRow
import hardcoder.dev.uikit.components.section.EmptyBlock
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun Diary(
    viewModel: DiaryViewModel,
    onGoBack: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
    onUpdateDiaryTrack: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
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
                },
            )
        },
    ) {
        val searchText = viewModel.searchTextInputController.state.collectAsState()

        ScaffoldWrapper(
            onFabClick = onCreateDiaryTrack,
            content = {
                DiaryContent(
                    diaryTrackLoadingController = viewModel.diaryTrackLoadingController,
                    filteredTrackLoadingController = viewModel.filteredTrackLoadingController,
                    tagMultiSelectionController = viewModel.tagMultiSelectionController,
                    searchText = searchText.value.input,
                    onUpdateDiaryTrack = onUpdateDiaryTrack,
                )
            },
            topBarConfig = TopBarConfig(
                type = TopBarType.SearchTopBarController(
                    controller = viewModel.searchTextInputController,
                    titleResId = R.string.diary_title_topBar,
                    placeholderText = R.string.diary_searchTrack_textField,
                    onGoBack = onGoBack,
                ),
            ),
            actionConfig = ActionConfig(
                listOf(
                    Action(
                        iconResId = R.drawable.ic_sort,
                        onActionClick = {
                            scope.launch {
                                filterBottomSheetState.toggle()
                            }
                        },
                    ),
                ),
            ),
        )
    }
}

@Composable
private fun DiaryContent(
    tagMultiSelectionController: MultiSelectionController<DiaryTag>,
    diaryTrackLoadingController: LoadingController<List<DiaryTrack>>,
    filteredTrackLoadingController: LoadingController<List<DiaryTrack>>,
    searchText: String,
    onUpdateDiaryTrack: (Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        LoadingContainer(
            controller1 = diaryTrackLoadingController,
            controller2 = filteredTrackLoadingController,
            loadedContent = { diaryItemsList, filteredDiaryTrackList ->
                val tagMultiSelectionControllerState = tagMultiSelectionController.state.collectAsState()

                when {
                    diaryItemsList.isEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        EmptySection(emptyTitleResId = R.string.diary_nowEmpty_text)
                    }

                    searchText.isEmpty() && tagMultiSelectionControllerState.value is MultiSelectionController.State.Empty -> {
                        DiaryTrackListSection(
                            items = diaryItemsList,
                            onUpdateTrack = onUpdateDiaryTrack,
                        )
                    }

                    filteredDiaryTrackList.isEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        EmptyBlock(
                            lottieAnimationResId = R.raw.empty_astronaut,
                            modifier = Modifier.height(100.dp),
                            emptyTitleResId = R.string.diary_nothingFound_text,
                        )
                    }

                    else -> {
                        DiaryTrackListSection(
                            items = filteredDiaryTrackList,
                            onUpdateTrack = onUpdateDiaryTrack,
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun ColumnScope.DiaryTrackListSection(
    items: List<DiaryTrack>,
    onUpdateTrack: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
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
            .padding(16.dp),
    ) {
        Row(Modifier.fillMaxWidth()) {
            Title(
                text = stringResource(R.string.diary_filterBottomSheetTitle_text),
                modifier = Modifier.weight(2f),
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
    val dateRangeFilterTypeResourcesProvider = koinInject<DateRangeFilterTypeResourcesProvider>()

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
        },
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
                name = tag.name,
            )
        },
    )
}