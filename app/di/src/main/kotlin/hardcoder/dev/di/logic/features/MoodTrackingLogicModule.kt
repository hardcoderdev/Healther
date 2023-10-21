package hardcoder.dev.di.logic.features

import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityCreator
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityDeleter
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityUpdater
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeUpdater
import hardcoder.dev.logics.features.moodTracking.moodType.PredefinedMoodTypeProvider
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logics.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import hardcoder.dev.logics.features.moodTracking.statistic.MoodTrackingStatisticProvider
import hardcoder.dev.resources.features.moodTracking.MoodActivityIconProvider
import hardcoder.dev.resources.features.moodTracking.MoodTypeIconProvider
import hardcoder.dev.resources.features.moodTracking.PredefinedMoodTypeProviderImpl
import hardcoder.dev.validators.features.moodTracking.MoodActivityNameValidator
import hardcoder.dev.validators.features.moodTracking.MoodTypeNameValidator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val moodTrackingLogicModule = module {
    singleOf(::MoodTypeIconProvider)
    singleOf(::MoodActivityNameValidator)
    singleOf(::MoodActivityIconProvider)
    singleOf(::MoodTypeNameValidator)
    singleOf(::MoodTrackDailyRateProvider)

    single {
        MoodActivityCreator(
            moodActivityDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityUpdater(
            moodActivityDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityDeleter(
            moodActivityDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityProvider(
            moodActivityDao = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodWithActivitiesProvider(
            moodWithActivityDao = get(),
            moodTrackProvider = get(),
            moodActivityProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodWithActivityCreator(
            moodWithActivityDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodWithActivityDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTrackCreator(
            moodTrackDao = get(),
            diaryTrackCreator = get(),
            moodWithActivityCreator = get(),
            moodTrackProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTrackUpdater(
            moodTrackDao = get(),
            diaryTrackDao = get(),
            diaryAttachmentDao = get(),
            dispatchers = get(),
            moodWithActivityCreator = get(),
            moodWithActivityDeleter = get(),
            attachmentTypeIdMapper = get(),
            diaryTrackUpdater = get(),
        )
    }

    single {
        MoodTrackDeleter(
            moodTrackDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTrackProvider(
            moodTrackDao = get(),
            moodTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeCreator(
            moodTypeDao = get(),
            predefinedMoodTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeUpdater(
            moodTypeDao = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeDeleter(
            moodTypeDao = get(),
            moodTrackDeleter = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeProvider(
            moodTypeDao = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }

    single<PredefinedMoodTypeProvider> {
        PredefinedMoodTypeProviderImpl(
            context = androidContext(),
            moodTypeIconProvider = get(),
        )
    }

    single {
        MoodTrackingStatisticProvider(
            moodTrackDao = get(),
            moodTypeProvider = get(),
            dispatchers = get(),
        )
    }
}