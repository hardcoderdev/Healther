package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.androidApp.ui.features.pedometer.logic.BatteryRequirementsController
import hardcoder.dev.androidApp.ui.features.pedometer.logic.PedometerManagerImpl
import hardcoder.dev.androidApp.ui.icons.IconProvider
import hardcoder.dev.database.AppDatabaseFactory
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.AppPreferenceUpdater
import hardcoder.dev.logic.appPreferences.PredefinedTracksManager
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import hardcoder.dev.logic.features.fasting.plan.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.statistic.FastingStatisticProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerStepHandler
import hardcoder.dev.logic.features.pedometer.PedometerStepProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackCreator
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.logic.features.waterBalance.WaterIntakeResolver
import hardcoder.dev.logic.features.waterBalance.WaterPercentageResolver
import hardcoder.dev.logic.features.waterBalance.WaterTrackCreator
import hardcoder.dev.logic.features.waterBalance.WaterTrackDeleter
import hardcoder.dev.logic.features.waterBalance.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterBalance.WaterTrackProvider
import hardcoder.dev.logic.features.waterBalance.WaterTrackUpdater
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeIconResourceValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.features.waterBalance.statistic.WaterTrackingStatisticProvider
import hardcoder.dev.logic.hero.GenderIdMapper
import hardcoder.dev.logic.hero.GenderProvider
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

    val predefinedTracksManager by lazy {
        PredefinedTracksManager(
            context = context,
            drinkTypeCreator = drinkTypeCreator
        )
    }

    val iconResourceProvider by lazy {
        IconProvider(context = context)
    }

    val genderProvider by lazy {
        GenderProvider()
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
            idGenerator = idGenerator,
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
        WaterTrackProvider(
            appDatabase = appDatabase,
            drinkTypeProvider = drinkTypeProvider
        )
    }

    val waterTrackingStatisticProvider by lazy {
        WaterTrackingStatisticProvider(
            appDatabase = appDatabase,
            drinkTypeProvider = drinkTypeProvider
        )
    }

    val drinkTypeNameValidator by lazy {
        DrinkTypeNameValidator()
    }

    val drinkTypeIconResourceValidator by lazy {
        DrinkTypeIconResourceValidator()
    }

    val drinkTypeCreator by lazy {
        DrinkTypeCreator(
            context = context,
            idGenerator = idGenerator,
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val drinkTypeUpdater by lazy {
        DrinkTypeUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val drinkTypeDeleter by lazy {
        DrinkTypeDeleter(
            appDatabase = appDatabase,
            waterTrackDeleter = waterTrackDeleter,
            dispatcher = Dispatchers.IO
        )
    }

    val drinkTypeProvider by lazy {
        DrinkTypeProvider(
            appDatabase = appDatabase
        )
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

    val batteryRequirementsController by lazy {
        BatteryRequirementsController()
    }

    val permissionsController by lazy {
        PermissionsController()
    }

    val pedometerManager by lazy {
        PedometerManagerImpl(
            context = context,
            permissionsController = permissionsController,
            batteryRequirementsController = batteryRequirementsController
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

    val pedometerStatisticProvider by lazy {
        PedometerStatisticProvider(
            appDatabase = appDatabase,
            kilometersResolver = kilometersResolver,
            caloriesResolver = caloriesResolver
        )
    }

    private val fastingPlanIdMapper by lazy {
        FastingPlanIdMapper()
    }

    val fastingStatisticProvider by lazy {
        FastingStatisticProvider(
            appDatabase = appDatabase,
            fastingPlanIdMapper = fastingPlanIdMapper,
            fastingTrackProvider = fastingTrackProvider,
            dateTimeProvider = dateTimeProvider
        )
    }

    val fastingTrackProvider by lazy {
        FastingTrackProvider(
            appDatabase = appDatabase,
            fastingPlanIdMapper = fastingPlanIdMapper
        )
    }

    val currentFastingManager by lazy {
        CurrentFastingManager(
            context = context,
            appDatabase = appDatabase,
            fastingPlanIdMapper = fastingPlanIdMapper,
            dispatcher = Dispatchers.IO,
            idGenerator = idGenerator,
            fastingTrackProvider = fastingTrackProvider
        )
    }

    val fastingPlanProvider by lazy {
        FastingPlanProvider()
    }

    val fastingPlanDurationResolver by lazy {
        FastingPlanDurationResolver()
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