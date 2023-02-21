package hardcoder.dev.healther.di

import android.content.Context
import hardcoder.dev.healther.data.AppDatabaseFactory
import hardcoder.dev.healther.logic.creators.AppPreferenceCreator
import hardcoder.dev.healther.logic.creators.HeroCreator
import hardcoder.dev.healther.logic.creators.WaterTrackCreator
import hardcoder.dev.healther.logic.deleters.WaterTrackDeleter
import hardcoder.dev.healther.logic.providers.AppPreferenceProvider
import hardcoder.dev.healther.logic.providers.HeroProvider
import hardcoder.dev.healther.logic.providers.WaterTrackProvider
import hardcoder.dev.healther.logic.resolvers.WaterIntakeResolver
import hardcoder.dev.healther.logic.resolvers.WaterPercentageResolver
import hardcoder.dev.healther.logic.updaters.AppPreferenceUpdater
import hardcoder.dev.healther.logic.updaters.HeroUpdater
import hardcoder.dev.healther.logic.updaters.WaterTrackUpdater
import hardcoder.dev.healther.logic.validators.WaterTrackMillilitersValidator
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