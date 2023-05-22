package hardcoder.dev.presentation.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManageTagsViewModel(diaryTagProvider: DiaryTagProvider) : ViewModel() {

    val state = diaryTagProvider.provideAllDiaryTags().map { diaryTagList ->
        State(diaryTagList = diaryTagList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            diaryTagList = emptyList()
        )
    )

    data class State(val diaryTagList: List<DiaryTag>)
}