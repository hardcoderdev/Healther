package hardcoder.dev.presentation.features.diary.tags

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider

class DiaryTagsViewModel(diaryTagProvider: DiaryTagProvider) : ScreenModel {

    val diaryTagsLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = diaryTagProvider.provideAllDiaryTags(),
    )
}