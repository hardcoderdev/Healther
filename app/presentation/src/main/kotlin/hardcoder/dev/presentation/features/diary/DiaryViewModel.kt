package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.entities.features.diary.DiaryTrack
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.features.diary.DateRangeFilterTypeProvider
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModel(
    private val dateRangeFilterTypeMapper: hardcoder.dev.mappers.features.diary.DateRangeFilterTypeMapper,
    private val diaryTrackProvider: DiaryTrackProvider,
    dateRangeFilterTypeProvider: DateRangeFilterTypeProvider,
    diaryTagProvider: DiaryTagProvider,
    dateTimeProvider: DateTimeProvider,
    appPreferenceProvider: AppPreferenceProvider,
) : ViewModel() {

    val dateRangeFilterTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = dateRangeFilterTypeProvider.provideAllDateRangeFilters(),
    )

    private val firstLaunchTime = appPreferenceProvider.provideAppPreference().map {
        requireNotNull(it!!.firstLaunchTime)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = dateTimeProvider.currentInstant(),
    )

    private val diaryTrackList = dateRangeFilterTypeSelectionController
        .selectedItemsOrEmptySet()
        .flatMapLatest { range ->
            diaryTrackProvider.provideAllDiaryTracksByDateRange(
                dateRangeFilterTypeMapper.map(
                    firstLaunchTime = firstLaunchTime.first(),
                    dateRangeFilterType = range ?: hardcoder.dev.entities.features.diary.DateRangeFilterType.BY_DAY,
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags(),
    )

    val searchTextInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = "",
    )

    val diaryTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTrackList,
    )

    val filteredTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = combine(
            diaryTrackList,
            searchTextInputController.state,
            tagMultiSelectionController.selectedItemsOrEmptySet(),
        ) { diaryTrackList, searchText, tags ->
            diaryTrackList.let {
                if (searchText.input.isEmpty()) {
                    it
                } else {
                    it.filterBySearchText(searchText.input)
                }
            }.let {
                if (tags.isEmpty()) {
                    it
                } else {
                    it.filterByTags(tags)
                }
            }
        },
    )

    private fun List<DiaryTrack>.filterByTags(
        selectedTagList: Set<DiaryTag>,
    ): List<DiaryTrack> {
        val filteredTrackList = mutableListOf<DiaryTrack>()

        forEach { diaryTrack ->
            diaryTrack.diaryAttachmentGroup?.tags?.let { tags ->
                if (tags.containsAll(selectedTagList)) {
                    filteredTrackList.add(diaryTrack)
                }
            }
        }

        return filteredTrackList
    }

    private fun List<DiaryTrack>.filterBySearchText(searchText: String): List<DiaryTrack> {
        val filteredTrackList = mutableListOf<DiaryTrack>()

        searchText.trim().split(" ").let { searchWord ->
            filteredTrackList.addAll(
                filter { diaryTrack ->
                    searchWord.all { diaryTrack.content.contains(it, ignoreCase = true) }
                },
            )
        }

        return filteredTrackList
    }
}