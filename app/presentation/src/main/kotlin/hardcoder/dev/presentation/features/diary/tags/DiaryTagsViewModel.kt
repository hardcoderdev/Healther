package hardcoder.dev.presentation.features.diary.tags

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.viewmodel.ViewModel

class DiaryTagsViewModel(diaryTagProvider: DiaryTagProvider) : ViewModel() {

    val diaryTagsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = diaryTagProvider.provideAllDiaryTags(),
    )
}