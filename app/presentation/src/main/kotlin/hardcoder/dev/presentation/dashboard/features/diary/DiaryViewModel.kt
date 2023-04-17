package hardcoder.dev.presentation.dashboard.features.diary

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
    private val dateRangeFilterTypes = dateRangeFilterTypeProvider.provideAllDateRangeFilters().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    private var selectedTagMutableList = mutableListOf<DiaryTag>()
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
        val filteredTracks = mutableListOf<DiaryTrack>()

        if (searchText.isNotEmpty()) {
            diaryTrackList.forEach {
                if (
                    it.description.trim().contains(other = searchText, ignoreCase = true)
                    || it.title?.trim()?.contains(other = searchText, ignoreCase = true) == true
                ) {
                    filteredTracks.add(it)
                }
            }
        } else {
            filteredTracks.addAll(diaryTrackList)
        }

        filteredTracks.removeAll {
            it.diaryAttachmentGroup?.tags?.let { tags ->
                !tags.containsAll(selectedTagList)
            } ?: false
        }

        State(
            tagList = tagList,
            searchText = searchText,
            filteredTrackList = filteredTracks,
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

    fun addFilterFeatureTag(diaryTag: DiaryTag) {
        if (selectedTagList.value.contains(diaryTag).not()) {
            selectedTagMutableList = selectedTagList.value.toMutableList()
            selectedTagMutableList.add(diaryTag)
            selectedTagList.value = selectedTagMutableList
        }
    }

    fun removeFilterFeatureTag(diaryTag: DiaryTag) {
        selectedTagMutableList = selectedTagList.value.toMutableList()
        selectedTagMutableList.remove(diaryTag)
        selectedTagList.value = selectedTagMutableList
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