package hardcoder.dev.presentation.dashboard.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType
import hardcoder.dev.logic.dashboard.features.DateRangeFilterTypeMapper
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTags
import hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType.DiaryWithFeatureTagsProvider
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTag
import hardcoder.dev.logic.dashboard.features.diary.featureType.FeatureTagProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModel(
    private val dateRangeFilterTypeMapper: DateRangeFilterTypeMapper,
    diaryWithFeatureTagsProvider: DiaryWithFeatureTagsProvider,
    featureTagProvider: FeatureTagProvider
) : ViewModel() {

    private val selectedDateRangeFilterType =
        MutableStateFlow<DateRangeFilterType>(DateRangeFilterType.ByDay)
    private var mutableSelectedFilters = mutableListOf<FeatureTag>()
    private val selectedFilterList = MutableStateFlow<List<FeatureTag>>(emptyList())
    private val searchText = MutableStateFlow("")
    private val diaryTrackList = selectedDateRangeFilterType.flatMapLatest {
        diaryWithFeatureTagsProvider.provideDiaryWithFeatureTagList(dateRangeFilterTypeMapper.map(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val filterList = featureTagProvider.provideAllFeatureTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val state = combine(
        filterList,
        selectedFilterList,
        searchText,
        selectedDateRangeFilterType,
        diaryTrackList
    ) { filterList, selectedFilterList, searchText, selectedDateFilterType,
        diaryTrackList ->
        val filteredTracks = mutableListOf<DiaryWithFeatureTags>()

        if (searchText.isNotEmpty()) {
            diaryTrackList.forEach {
                if (
                    it.diaryTrack.text.trim().contains(other = searchText, ignoreCase = true)
                    || it.diaryTrack.title?.trim()?.contains(other = searchText, ignoreCase = true) == true
                ) {
                    filteredTracks.add(it)
                }
            }
        } else {
            filteredTracks.addAll(diaryTrackList)
        }

        if (selectedFilterList.isNotEmpty()) {
            filteredTracks.removeAll { item ->
                !item.featureTags.containsAll(selectedFilterList)
            }
        }

        State(
            filterList = filterList,
            searchText = searchText,
            filteredTrackList = filteredTracks,
            selectedDateRangeFilterType = selectedDateFilterType,
            diaryTrackList = diaryTrackList,
            selectedFilterList = selectedFilterList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            filterList = filterList.value,
            searchText = searchText.value,
            filteredTrackList = emptyList(),
            selectedDateRangeFilterType = selectedDateRangeFilterType.value,
            diaryTrackList = diaryTrackList.value,
            selectedFilterList = selectedFilterList.value
        )
    )

    fun updateSelectedFilterType(type: DateRangeFilterType) {
        selectedDateRangeFilterType.value = type
    }

    fun updateSearchText(text: String) {
        searchText.value = text
    }

    fun addFilterFeatureTag(featureTag: FeatureTag) {
        if (selectedFilterList.value.contains(featureTag).not()) {
            mutableSelectedFilters = selectedFilterList.value.toMutableList()
            mutableSelectedFilters.add(featureTag)
            selectedFilterList.value = mutableSelectedFilters
        }
    }

    fun removeFilterFeatureTag(featureTag: FeatureTag) {
        mutableSelectedFilters = selectedFilterList.value.toMutableList()
        mutableSelectedFilters.remove(featureTag)
        selectedFilterList.value = mutableSelectedFilters
    }


    data class State(
        val selectedFilterList: List<FeatureTag>,
        val filterList: List<FeatureTag>,
        val selectedDateRangeFilterType: DateRangeFilterType,
        val searchText: String,
        val diaryTrackList: List<DiaryWithFeatureTags>,
        val filteredTrackList: List<DiaryWithFeatureTags>
    )
}