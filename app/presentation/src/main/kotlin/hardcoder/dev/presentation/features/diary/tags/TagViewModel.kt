package hardcoder.dev.presentation.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider

class TagViewModel(diaryTagProvider: DiaryTagProvider) : ViewModel() {

    val diaryTagsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTagProvider.provideAllDiaryTags()
    )
}