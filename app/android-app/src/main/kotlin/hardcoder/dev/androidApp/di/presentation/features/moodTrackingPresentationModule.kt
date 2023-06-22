package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.ActivityCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.ActivityViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.ActivityUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreationViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moodTrackingPresentationModule = module {
    viewModel {
        MoodTrackingViewModel(
            moodWithActivitiesProvider = get(),
            moodTrackProvider = get(),
            dateTimeProvider = get(),
            moodTrackingStatisticProvider = get(),
        )
    }

    viewModel {
        MoodTrackingCreationViewModel(
            moodTrackCreator = get(),
            moodTypeProvider = get(),
            activityProvider = get(),
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
            activityProvider = get(),
            moodTypeProvider = get(),
        )
    }

    viewModel {
        MoodTrackingHistoryViewModel(
            moodWithActivitiesProvider = get(),
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
        ActivityViewModel(
            activityProvider = get(),
        )
    }

    viewModel {
        ActivityCreationViewModel(
            activityCreator = get(),
            activityNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        ActivityUpdateViewModel(
            activityId = parameters.get(),
            activityNameValidator = get(),
            activityDeleter = get(),
            activityUpdater = get(),
            activityProvider = get(),
            iconResourceProvider = get(),
        )
    }
}