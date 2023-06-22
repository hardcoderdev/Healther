package hardcoder.dev.androidApp.di.logic.features

import hardcoder.dev.androidApp.ui.features.moodTracking.activity.providers.ActivityIconProvider
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.providers.MoodTypeIconProvider
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.providers.PredefinedMoodTypeProviderImpl
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityDeleter
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityNameValidator
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityUpdater
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.PredefinedMoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityCreator
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivityDeleter
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatisticProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val moodTrackingLogicModule = module {
    singleOf(::MoodTypeIconProvider)
    singleOf(::MoodActivityNameValidator)
    singleOf(::ActivityIconProvider)
    singleOf(::MoodTypeNameValidator)

    single {
        MoodActivityCreator(
            idGenerator = get(),
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodActivityProvider(
            appDatabase = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodWithActivitiesProvider(
            appDatabase = get(),
            moodTrackProvider = get(),
            moodActivityProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodWithActivityCreator(
            appDatabase = get(),
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
            appDatabase = get(),
            idGenerator = get(),
            diaryTrackCreator = get(),
            moodWithActivityCreator = get(),
            moodTrackProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTrackUpdater(
            appDatabase = get(),
            dispatchers = get(),
            moodWithActivityCreator = get(),
            moodWithActivityDeleter = get(),
            attachmentTypeIdMapper = get(),
            diaryTrackCreator = get(),
        )
    }

    single {
        MoodTrackDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTrackProvider(
            appDatabase = get(),
            moodTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeCreator(
            idGenerator = get(),
            appDatabase = get(),
            predefinedMoodTypeProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeDeleter(
            appDatabase = get(),
            moodTrackDeleter = get(),
            dispatchers = get(),
        )
    }

    single {
        MoodTypeProvider(
            appDatabase = get(),
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
            appDatabase = get(),
            moodTypeProvider = get(),
            dispatchers = get(),
        )
    }
}