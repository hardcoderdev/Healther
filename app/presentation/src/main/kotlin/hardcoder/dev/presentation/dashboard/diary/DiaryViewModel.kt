package hardcoder.dev.presentation.dashboard.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType
import hardcoder.dev.logic.dashboard.features.DateRangeFilterTypeMapper
import hardcoder.dev.logic.dashboard.features.DateRangeFilterTypeProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModel(
    private val dateRangeFilterTypeMapper: DateRangeFilterTypeMapper,
    private val diaryTrackProvider: DiaryTrackProvider,
    dateRangeFilterTypeProvider: DateRangeFilterTypeProvider,
    diaryTagProvider: DiaryTagProvider
) : ViewModel() {

    private val selectedDateRangeFilterType = MutableStateFlow(DateRangeFilterType.BY_DAY)
    private val dateRangeFilterTypes =
        dateRangeFilterTypeProvider.provideAllDateRangeFilters().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    private val selectedTagList = MutableStateFlow<List<DiaryTag>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val diaryTrackList = selectedDateRangeFilterType.flatMapLatest {
        diaryTrackProvider.provideAllDiaryTracksByDateRange(dateRangeFilterTypeMapper.map(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val tagList = diaryTagProvider.provideAllDiaryTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        tagList,
        selectedTagList,
        searchText,
        dateRangeFilterTypes,
        selectedDateRangeFilterType,
        diaryTrackList
    ) { tagList, selectedTagList, searchText, dateRangeFilterTypes,
        selectedDateRangeFilterType, diaryTrackList ->
        State(
            tagList = tagList,
            searchText = searchText,
            filteredTrackList = diaryTrackList.filterTracks(searchText),
            dateRangeFilterTypes = dateRangeFilterTypes,
            selectedDateRangeFilterType = selectedDateRangeFilterType,
            diaryTrackList = diaryTrackList,
            selectedTagList = selectedTagList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            tagList = tagList.value,
            searchText = searchText.value,
            filteredTrackList = emptyList(),
            dateRangeFilterTypes = dateRangeFilterTypes.value,
            selectedDateRangeFilterType = selectedDateRangeFilterType.value,
            diaryTrackList = diaryTrackList.value,
            selectedTagList = selectedTagList.value
        )
    )

    fun updateSelectedFilterType(type: DateRangeFilterType) {
        selectedDateRangeFilterType.value = type
    }

    fun updateSearchText(text: String) {
        searchText.value = text
    }

    fun toggleFilterTag(diaryTag: DiaryTag) {
        val selectedTagMutableList = selectedTagList.value.toMutableList()
        val isRemoved = selectedTagMutableList.removeIf { it == diaryTag }
        if (isRemoved) {
            selectedTagList.value = selectedTagMutableList
            return
        } else {
            selectedTagMutableList.add(diaryTag)
            selectedTagList.value = selectedTagMutableList
        }
    }

    private fun List<DiaryTrack>.filterTracks(searchText: String): List<DiaryTrack> {
        return if (searchText.isNotEmpty()) {
            val searchedDiaryTrackList = searchDiaryTracks(searchText, this)
            return if (selectedTagList.value.isNotEmpty()) {
                filterDiaryTracksByTags(searchedDiaryTrackList)
            } else {
                searchedDiaryTrackList
            }
        } else {
            filterDiaryTracksByTags(this)
        }
    }

    private fun filterDiaryTracksByTags(diaryTrackList: List<DiaryTrack>): List<DiaryTrack> {
        val filteredTrackList = mutableListOf<DiaryTrack>()

        diaryTrackList.forEach { diaryTrack ->
            diaryTrack.diaryAttachmentGroup?.tags?.let { tags ->
                if (tags.containsAll(selectedTagList.value)) {
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

    data class State(
        val dateRangeFilterTypes: List<DateRangeFilterType>,
        val selectedDateRangeFilterType: DateRangeFilterType,
        val searchText: String,
        val tagList: List<DiaryTag>,
        val selectedTagList: List<DiaryTag>,
        val diaryTrackList: List<DiaryTrack>,
        val filteredTrackList: List<DiaryTrack>
    )
}