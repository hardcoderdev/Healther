package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.moodTracking.MoodTrackingAnalyticsViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivitiesViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moodTrackingPresentationModule = module {
    viewModel {
        MoodTrackingViewModel(
            moodWithActivitiesProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        MoodTrackingAnalyticsViewModel(
            moodTrackProvider = get(),
            moodTrackingStatisticProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        MoodTrackingCreationViewModel(
            moodTrackCreator = get(),
            moodTypeProvider = get(),
            moodActivityProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel { parameters ->
        MoodTrackingUpdateViewModel(
            moodTrackId = parameters.get(),
            moodTrackUpdater = get(),
            moodTrackDeleter = get(),
            diaryTrackProvider = get(),
            moodTrackProvider = get(),
            diaryAttachmentProvider = get(),
            moodWithActivityProvider = get(),
            moodActivityProvider = get(),
            moodTypeProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        MoodTrackingHistoryViewModel(
            moodWithActivitiesProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        MoodTypesViewModel(
            moodTypeProvider = get(),
        )
    }

    viewModel {
        MoodTypeCreationViewModel(
            moodTypeCreator = get(),
            moodTypeNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        MoodTypeUpdateViewModel(
            moodTypeId = parameters.get(),
            iconResourceProvider = get(),
            moodTypeNameValidator = get(),
            moodTypeProvider = get(),
            moodTypeUpdater = get(),
            moodTypeDeleter = get(),
        )
    }

    viewModel {
        MoodActivitiesViewModel(
            moodActivityProvider = get(),
        )
    }

    viewModel {
        MoodActivityCreationViewModel(
            moodActivityCreator = get(),
            moodActivityNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        MoodActivityUpdateViewModel(
            activityId = parameters.get(),
            moodActivityNameValidator = get(),
            moodActivityDeleter = get(),
            moodActivityUpdater = get(),
            moodActivityProvider = get(),
            iconResourceProvider = get(),
        )
    }
}