package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.diary.DiaryCreationViewModel
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagCreationViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagUpdateViewModel
import hardcoder.dev.presentation.features.diary.tags.DiaryTagsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diaryPresentationModule = module {
    viewModel {
        DiaryViewModel(
            dateRangeFilterTypeMapper = get(),
            dateRangeFilterTypeProvider = get(),
            diaryTrackProvider = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
            currencyProvider = get(),
            currencyCollector = get(),
            experienceCollector = get(),
        )
    }

    viewModel {
        DiaryCreationViewModel(
            diaryTrackCreator = get(),
            diaryTrackContentValidator = get(),
            diaryTagProvider = get(),
            dateTimeProvider = get(),
            currencyCalculator = get(),
            currencyCreator = get(),
            diaryDailyRateProvider = get(),
            diaryTrackProvider = get(),
            experienceCreator = get(),
            experienceCalculator = get(),
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