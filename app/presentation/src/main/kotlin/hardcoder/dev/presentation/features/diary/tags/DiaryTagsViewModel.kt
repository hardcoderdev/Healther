package hardcoder.dev.presentation.features.diary.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider

class DiaryTagsViewModel(diaryTagProvider: DiaryTagProvider) : ViewModel() {

    val diaryTagsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTagProvider.provideAllDiaryTags(),
    )
}