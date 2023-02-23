package hardcoder.dev.di

import android.content.Context
import hardcoder.dev.database.AppDatabaseFactory
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.hero.GenderIdMapper
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.hero.HeroUpdater
import hardcoder.dev.logic.waterBalance.DrinkTypeIdMapper
import hardcoder.dev.logic.waterBalance.DrinkTypeProvider
import hardcoder.dev.logic.waterBalance.WaterTrackCreator
import hardcoder.dev.logic.waterBalance.WaterTrackDeleter
import hardcoder.dev.logic.waterBalance.WaterTrackProvider
import hardcoder.dev.logic.waterBalance.WaterTrackUpdater
import hardcoder.dev.logic.waterBalance.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.waterBalance.resolvers.WaterPercentageResolver
import hardcoder.dev.logic.waterBalance.validators.WaterTrackMillilitersValidator
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

    private val drinkTypeIdMapper by lazy {
        DrinkTypeIdMapper()
    }

    val waterTrackCreator by lazy {
        WaterTrackCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            drinkTypeIdMapper = drinkTypeIdMapper
        )
    }

    val waterTrackUpdater by lazy {
        WaterTrackUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            drinkTypeIdMapper = drinkTypeIdMapper
        )
    }

    val waterTrackDeleter by lazy {
        WaterTrackDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val waterTrackProvider by lazy {
        WaterTrackProvider(
            appDatabase = appDatabase,
            drinkTypeIdMapper = drinkTypeIdMapper
        )
    }

    val drinkTypeProvider by lazy {
        DrinkTypeProvider()
    }

    private val genderIdMapper by lazy {
        GenderIdMapper()
    }

    val heroCreator by lazy {
        HeroCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            genderIdMapper = genderIdMapper
        )
    }

    val heroUpdater by lazy {
        HeroUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            genderIdMapper = genderIdMapper
        )
    }

    val heroProvider by lazy {
        HeroProvider(
            appDatabase = appDatabase,
            genderIdMapper = genderIdMapper
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