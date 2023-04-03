package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.androidApp.ui.features.moodTracking.hobby.HobbyIconProvider
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.MoodTypeIconProvider
import hardcoder.dev.androidApp.ui.features.pedometer.logic.BatteryRequirementsController
import hardcoder.dev.androidApp.ui.features.pedometer.logic.PedometerManagerImpl
import hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.DrinkTypeIconProvider
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
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyCreator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyDeleter
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyNameValidator
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyProvider
import hardcoder.dev.logic.features.moodTracking.hobby.HobbyUpdater
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeDeleter
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeNameValidator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeUpdater
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyCreator
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyDeleter
import hardcoder.dev.logic.features.moodTracking.moodWithHobby.MoodWithHobbyProvider
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatisticProvider
import hardcoder.dev.logic.features.pedometer.CaloriesResolver
import hardcoder.dev.logic.features.pedometer.KilometersResolver
import hardcoder.dev.logic.features.pedometer.PedometerStepHandler
import hardcoder.dev.logic.features.pedometer.PedometerStepProvider
import hardcoder.dev.logic.features.pedometer.PedometerTrackCreator
import hardcoder.dev.logic.features.pedometer.PedometerTrackProvider
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatisticProvider
import hardcoder.dev.logic.features.waterTracking.WaterIntakeResolver
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackCreator
import hardcoder.dev.logic.features.waterTracking.WaterTrackDeleter
import hardcoder.dev.logic.features.waterTracking.WaterTrackMillilitersValidator
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackUpdater
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeDeleter
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeNameValidator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeUpdater
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
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
            drinkTypeCreator = drinkTypeCreator,
            moodTypeCreator = moodTypeCreator
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

    val drinkTypeCreator by lazy {
        DrinkTypeCreator(
            context = context,
            idGenerator = idGenerator,
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            iconResourceProvider = drinkTypeIconProvider
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
            appDatabase = appDatabase,
            iconResourceProvider = drinkTypeIconProvider
        )
    }

    val drinkTypeIconProvider by lazy {
        DrinkTypeIconProvider()
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

    val hobbyCreator by lazy {
        HobbyCreator(
            idGenerator = idGenerator,
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val hobbyUpdater by lazy {
        HobbyUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val hobbyDeleter by lazy {
        HobbyDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val hobbyProvider by lazy {
        HobbyProvider(
            appDatabase = appDatabase,
            iconResourceProvider = hobbyIconProvider
        )
    }

    val hobbyNameValidator by lazy {
        HobbyNameValidator()
    }

    val hobbyIconProvider by lazy {
        HobbyIconProvider()
    }

    val moodWithHobbyProvider by lazy {
        MoodWithHobbyProvider(
            appDatabase = appDatabase,
            moodTrackProvider = moodTrackProvider,
            hobbyProvider = hobbyProvider
        )
    }

    val moodWithHobbyCreator by lazy {
        MoodWithHobbyCreator(
            idGenerator = idGenerator,
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val moodWithHobbyDeleter by lazy {
        MoodWithHobbyDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val moodTrackCreator by lazy {
        MoodTrackCreator(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            idGenerator = idGenerator
        )
    }

    val moodTrackUpdater by lazy {
        MoodTrackUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            moodWithHobbyCreator = moodWithHobbyCreator,
            moodWithHobbyDeleter = moodWithHobbyDeleter
        )
    }

    val moodTrackDeleter by lazy {
        MoodTrackDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val moodTrackProvider by lazy {
        MoodTrackProvider(
            appDatabase = appDatabase,
            moodTypeProvider = moodTypeProvider
        )
    }

    val moodTypeNameValidator by lazy {
        MoodTypeNameValidator()
    }

    val moodTypeCreator by lazy {
        MoodTypeCreator(
            context = context,
            idGenerator = idGenerator,
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            iconResourceProvider = moodTypeIconProvider
        )
    }

    val moodTypeUpdater by lazy {
        MoodTypeUpdater(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO
        )
    }

    val moodTypeDeleter by lazy {
        MoodTypeDeleter(
            appDatabase = appDatabase,
            dispatcher = Dispatchers.IO,
            moodTrackDeleter = moodTrackDeleter
        )
    }

    val moodTypeProvider by lazy {
        MoodTypeProvider(
            appDatabase = appDatabase,
            iconResourceProvider = moodTypeIconProvider
        )
    }

    val moodTypeIconProvider by lazy {
        MoodTypeIconProvider()
    }

    val moodTrackingStatisticProvider by lazy {
        MoodTrackingStatisticProvider(
            appDatabase = appDatabase,
            moodTypeProvider = moodTypeProvider
        )
    }
}