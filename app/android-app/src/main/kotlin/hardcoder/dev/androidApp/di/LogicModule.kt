package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.androidApp.ui.features.pedometer.logic.BatteryRequirements
import hardcoder.dev.androidApp.ui.features.pedometer.logic.PedometerManagerImpl
import hardcoder.dev.database.AppDatabaseFactory
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerStepHandler
import hardcoder.dev.logic.features.pedometer.PedometerStepProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackCreator
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.starvation.DateTimeProvider
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanDurationResolver
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanProvider
import hardcoder.dev.logic.features.starvation.statistic.StarvationStatisticProvider
import hardcoder.dev.logic.features.starvation.track.CurrentStarvationManager
import hardcoder.dev.logic.features.starvation.track.StarvationTrackProvider
import hardcoder.dev.logic.features.waterBalance.DrinkTypeIdMapper
import hardcoder.dev.logic.features.waterBalance.DrinkTypeProvider
import hardcoder.dev.logic.features.waterBalance.WaterTrackCreator
import hardcoder.dev.logic.features.waterBalance.WaterTrackDeleter
import hardcoder.dev.logic.features.waterBalance.WaterTrackProvider
import hardcoder.dev.logic.features.waterBalance.WaterTrackUpdater
import hardcoder.dev.logic.features.waterBalance.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.features.waterBalance.resolvers.WaterPercentageResolver
import hardcoder.dev.logic.features.waterBalance.validators.WaterTrackMillilitersValidator
import hardcoder.dev.logic.hero.GenderIdMapper
import hardcoder.dev.logic.hero.HeroCreator
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.hero.HeroUpdater
import hardcoder.dev.permissions.PermissionsController
import kotlinx.coroutines.Dispatchers

class LogicModule(private val context: Context) {

    private val appDatabase by lazy {
        AppDatabaseFactory.create(
            context,
            name = "healther_db"
        )
    }

    private val idGenerator by lazy {
        IdGenerator(context)
    }

    private val timeUnitMapper by lazy {
        TimeUnitMapper()
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

    val kilometersResolver by lazy {
        KilometersResolver()
    }

    val caloriesResolver by lazy {
        CaloriesResolver()
    }

    private val batteryRequirements by lazy {
        BatteryRequirements(context)
    }

    val permissionsController by lazy {
        PermissionsController()
    }

    val pedometerManager by lazy {
        PedometerManagerImpl(
            context = context,
            permissionsController = permissionsController,
            batteryRequirements = batteryRequirements
        )
    }

    private val pedometerTrackCreator by lazy {
        PedometerTrackCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val pedometerStepHandler by lazy {
        PedometerStepHandler(
            idGenerator = idGenerator,
            pedometerTrackCreator = pedometerTrackCreator
        )
    }

    val pedometerStepProvider by lazy {
        PedometerStepProvider(
            pedometerTrackProvider = pedometerTrackProvider
        )
    }

    val pedometerTrackProvider by lazy {
        PedometerTrackProvider(
            appDatabase = appDatabase
        )
    }


    private val starvationPlanIdMapper by lazy {
        StarvationPlanIdMapper()
    }

    val starvationStatisticProvider by lazy {
        StarvationStatisticProvider(
            appDatabase = appDatabase,
            starvationPlanIdMapper = starvationPlanIdMapper,
            timeUnitMapper = timeUnitMapper,
            starvationTrackProvider = starvationTrackProvider,
            dateTimeProvider = dateTimeProvider
        )
    }

    val starvationTrackProvider by lazy {
        StarvationTrackProvider(
            appDatabase = appDatabase,
            starvationPlanIdMapper = starvationPlanIdMapper
        )
    }

    val currentStarvationManager by lazy {
        CurrentStarvationManager(
            context = context,
            appDatabase = appDatabase,
            starvationPlanIdMapper = starvationPlanIdMapper,
            dispatcher = Dispatchers.IO,
            idGenerator = idGenerator,
            starvationTrackProvider = starvationTrackProvider
        )
    }

    val starvationPlanProvider by lazy {
        StarvationPlanProvider()
    }

    val starvationPlanDurationResolver by lazy {
        StarvationPlanDurationResolver(
            timeUnitMapper = timeUnitMapper
        )
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

    val dateTimeProvider by lazy {
        DateTimeProvider(updatePeriodMillis = 1000)
    }
}