package hardcoder.dev.di

import android.content.Context
import hardcoder.dev.logic.validators.WaterTrackMillilitersValidator
import hardcoder.dev.logic.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.creators.WaterTrackCreator
import hardcoder.dev.logic.creators.HeroCreator
import hardcoder.dev.logic.creators.AppPreferenceCreator
import hardcoder.dev.logic.updaters.WaterTrackUpdater
import hardcoder.dev.logic.updaters.HeroUpdater
import hardcoder.dev.logic.updaters.AppPreferenceUpdater
import hardcoder.dev.logic.deleters.WaterTrackDeleter
import hardcoder.dev.logic.providers.WaterTrackProvider
import hardcoder.dev.logic.providers.HeroProvider
import hardcoder.dev.logic.providers.AppPreferenceProvider
import hardcoder.dev.database.AppDatabaseFactory
import hardcoder.dev.logic.resolvers.WaterPercentageResolver
import kotlinx.coroutines.Dispatchers

class LogicModule(private val context: Context) {

    private val appDatabase by lazy {
        AppDatabaseFactory.create(
            context,
            name = "healther_db"
        )
    }

    val waterPercentageResolver by lazy {
        WaterPercentageResolver()
    }

    val waterIntakeResolver by lazy {
        WaterIntakeResolver()
    }

    val waterTrackMillilitersValidator by lazy {
        WaterTrackMillilitersValidator()
    }

    val waterTrackCreator by lazy {
        WaterTrackCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val waterTrackUpdater by lazy {
        WaterTrackUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val waterTrackDeleter by lazy {
        WaterTrackDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val waterTrackProvider by lazy {
        WaterTrackProvider(appDatabase = appDatabase)
    }

    val heroCreator by lazy {
        HeroCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val heroUpdater by lazy {
        HeroUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val heroProvider by lazy {
        HeroProvider(appDatabase = appDatabase)
    }

    val appPreferenceCreator by lazy {
        AppPreferenceCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val appPreferenceUpdater by lazy {
        AppPreferenceUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val appPreferenceProvider by lazy {
        AppPreferenceProvider(appDatabase = appDatabase)
    }
}