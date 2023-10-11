package hardcoder.dev.di.presentation.features

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
import org.koin.dsl.module

internal val moodTrackingPresentationModule = module {
    factory {
        MoodTrackingViewModel(
            moodWithActivitiesProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        MoodTrackingAnalyticsViewModel(
            moodTrackProvider = get(),
            moodTrackingStatisticProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        MoodTrackingCreationViewModel(
            moodTrackCreator = get(),
            moodTypeProvider = get(),
            moodActivityProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory { parameters ->
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

    factory {
        MoodTrackingHistoryViewModel(
            moodWithActivitiesProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        MoodTypesViewModel(
            moodTypeProvider = get(),
        )
    }

    factory {
        MoodTypeCreationViewModel(
            moodTypeCreator = get(),
            moodTypeNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    factory { parameters ->
        MoodTypeUpdateViewModel(
            moodTypeId = parameters.get(),
            iconResourceProvider = get(),
            moodTypeNameValidator = get(),
            moodTypeProvider = get(),
            moodTypeUpdater = get(),
            moodTypeDeleter = get(),
        )
    }

    factory {
        MoodActivitiesViewModel(
            moodActivityProvider = get(),
        )
    }

    factory {
        MoodActivityCreationViewModel(
            moodActivityCreator = get(),
            moodActivityNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    factory { parameters ->
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