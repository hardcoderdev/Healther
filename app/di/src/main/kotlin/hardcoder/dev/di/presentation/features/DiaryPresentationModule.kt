package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagCreationViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagUpdateViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagsViewModel
import org.koin.dsl.module

internal val diaryPresentationModule = module {
    factory {
        DiaryViewModel(
            dateRangeFilterTypeMapper = get(),
            dateRangeFilterTypeProvider = get(),
            diaryTrackProvider = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
            appPreferenceProvider = get(),
        )
    }

    factory {
        DiaryCreationViewModel(
            diaryTrackCreator = get(),
            diaryTrackContentValidator = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory { parameters ->
        DiaryUpdateViewModel(
            diaryTrackId = parameters.get(),
            diaryTrackUpdater = get(),
            diaryTrackProvider = get(),
            diaryTrackDeleter = get(),
            diaryTagProvider = get(),
            diaryTrackContentValidator = get(),
        )
    }

    factory {
        DiaryTagsViewModel(
            diaryTagProvider = get(),
        )
    }

    factory {
        DiaryTagCreationViewModel(
            diaryTagCreator = get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    factory { parameters ->
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