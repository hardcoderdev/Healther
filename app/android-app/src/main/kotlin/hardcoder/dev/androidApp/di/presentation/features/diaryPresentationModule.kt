package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.presentation.features.diary.tags.TagCreationViewModel
import hardcoder.dev.presentation.features.diary.tags.TagViewModel
import hardcoder.dev.presentation.features.diary.tags.TagUpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diaryPresentationModule = module {
    viewModel {
        DiaryViewModel(
            dateRangeFilterTypeMapper = get(),
            dateRangeFilterTypeProvider = get(),
            diaryTrackProvider = get(),
            diaryTagProvider = get(),
        )
    }

    viewModel {
        DiaryCreationViewModel(
            diaryTrackCreator = get(),
            diaryTrackContentValidator = get(),
            diaryTagProvider = get(),
        )
    }

    viewModel { parameters ->
        DiaryUpdateViewModel(
            diaryTrackId = parameters.get(),
            diaryTrackUpdater = get(),
            diaryTrackProvider = get(),
            diaryTrackDeleter = get(),
            diaryTagProvider = get(),
            diaryTrackContentValidator = get(),
        )
    }

    viewModel {
        TagViewModel(
            diaryTagProvider = get(),
        )
    }

    viewModel {
        TagCreationViewModel(
            diaryTagCreator = get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        TagUpdateViewModel(
            tagId = parameters.get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
            diaryTagProvider = get(),
            diaryTagUpdater = get(),
            diaryTagDeleter = get(),
        )
    }
}