package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagCreationViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagUpdateViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val diaryPresentationModule = module {
    viewModel {
        DiaryViewModel(
            dateRangeFilterTypeMapper = get(),
            dateRangeFilterTypeProvider = get(),
            diaryTrackProvider = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
            appPreferenceProvider = get(),
        )
    }

    viewModel {
        DiaryCreationViewModel(
            diaryTrackCreator = get(),
            diaryTrackContentValidator = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
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
        DiaryTagsViewModel(
            diaryTagProvider = get(),
        )
    }

    viewModel {
        DiaryTagCreationViewModel(
            diaryTagCreator = get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        DiaryTagUpdateViewModel(
            tagId = parameters.get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
            diaryTagProvider = get(),
            diaryTagUpdater = get(),
            diaryTagDeleter = get(),
        )
    }
}