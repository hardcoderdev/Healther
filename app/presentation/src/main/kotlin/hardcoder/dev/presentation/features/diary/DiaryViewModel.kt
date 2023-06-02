package hardcoder.dev.presentation.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.selectedItemsOrEmptySet
import hardcoder.dev.logic.features.diary.DateRangeFilterType
import hardcoder.dev.logic.features.diary.DateRangeFilterTypeMapper
import hardcoder.dev.logic.features.diary.DateRangeFilterTypeProvider
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModel(
    private val dateRangeFilterTypeMapper: DateRangeFilterTypeMapper,
    private val diaryTrackProvider: DiaryTrackProvider,
    dateRangeFilterTypeProvider: DateRangeFilterTypeProvider,
    diaryTagProvider: DiaryTagProvider
) : ViewModel() {

    private val selectedDateRangeFilterType = MutableStateFlow(DateRangeFilterType.BY_DAY)

    private val diaryTrackList = selectedDateRangeFilterType.flatMapLatest {
        diaryTrackProvider.provideAllDiaryTracksByDateRange(dateRangeFilterTypeMapper.map(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val dateRangeFilterTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = dateRangeFilterTypeProvider.provideAllDateRangeFilters()
    )

    val tagMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = diaryTagProvider.provideAllDiaryTags()
    )

    val searchTextInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val diaryTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTrackList
    )

    val filteredTrackLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTrackList.map { diaryTrackList ->
            diaryTrackList.filterTracks(
                searchTextInputController.state.value.input
            )
        }
    )

    private fun List<DiaryTrack>.filterTracks(searchText: String): List<DiaryTrack> {
        val selectedTagList = tagMultiSelectionController.selectedItemsOrEmptySet()

        return if (searchText.isNotEmpty()) {
            val searchedDiaryTrackList = searchDiaryTracks(searchText, this)
            return if (selectedTagList.isNotEmpty()) {
                filterDiaryTracksByTags(searchedDiaryTrackList)
            } else {
                searchedDiaryTrackList
            }
        } else {
            filterDiaryTracksByTags(this)
        }
    }

    private fun filterDiaryTracksByTags(diaryTrackList: List<DiaryTrack>): List<DiaryTrack> {
        val selectedTagList = tagMultiSelectionController.selectedItemsOrEmptySet()
        val filteredTrackList = mutableListOf<DiaryTrack>()

        diaryTrackList.forEach { diaryTrack ->
            diaryTrack.diaryAttachmentGroup?.tags?.let { tags ->
                if (tags.containsAll(selectedTagList)) {
                    filteredTrackList.add(diaryTrack)
                }
            }
        }

        return filteredTrackList
    }

    private fun searchDiaryTracks(
        searchText: String,
        diaryTrackList: List<DiaryTrack>
    ): MutableList<DiaryTrack> {
        val filteredTrackList = mutableListOf<DiaryTrack>()

        searchText.trim().split(" ").let { searchWord ->
            filteredTrackList.addAll(
                diaryTrackList.filter { diaryTrack ->
                    searchWord.all { diaryTrack.content.contains(it, ignoreCase = true) }
                }
            )
        }

        return filteredTrackList
    }
}