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
    single {
        DiaryViewModel(
            dateRangeFilterTypeMapper = get(),
            dateRangeFilterTypeProvider = get(),
            diaryTrackProvider = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
            appPreferenceProvider = get(),
        )
    }

    single {
        DiaryCreationViewModel(
            diaryTrackCreator = get(),
            diaryTrackContentValidator = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
        )
    }

    single { parameters ->
        DiaryUpdateViewModel(
            diaryTrackId = parameters.get(),
            diaryTrackUpdater = get(),
            diaryTrackProvider = get(),
            diaryTrackDeleter = get(),
            diaryTagProvider = get(),
            diaryTrackContentValidator = get(),
        )
    }

    single {
        DiaryTagsViewModel(
            diaryTagProvider = get(),
        )
    }

    single {
        DiaryTagCreationViewModel(
            diaryTagCreator = get(),
            diaryTagNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    single { parameters ->
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