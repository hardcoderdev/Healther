package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.presentation.features.fasting.FastingTrackCreateViewModel
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.CreateActivityViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.ManageActivitiesViewModel
import hardcoder.dev.presentation.features.moodTracking.activity.UpdateActivityViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeManageTracksViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackCreateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreateViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeManageViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterWeightViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.presentation.setUpFlow.SplashViewModel

class PresentationModule(
    val context: Context,
    val logicModule: LogicModule
) {

    fun getSplashViewModel() = SplashViewModel(
        appPreferenceProvider = logicModule.appPreferenceProvider
    )

    fun getEnterWeightViewModel() = EnterWeightViewModel()

    fun getSelectGenderViewModel() = SelectGenderViewModel(
        genderProvider = logicModule.genderProvider
    )

    fun getEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel()

    fun getHeroCreateViewModel() = HeroCreateViewModel(
        heroCreator = logicModule.heroCreator,
        appPreferenceUpdater = logicModule.appPreferenceUpdater
    )

    fun getWaterTrackingViewModel() = WaterTrackingViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterPercentageResolver = logicModule.waterPercentageResolver,
        waterTrackingStatisticProvider = logicModule.waterTrackingStatisticProvider
    )

    fun getDrinkTypeManageTracksViewModel() = DrinkTypeManageViewModel(
        drinkTypeProvider = logicModule.drinkTypeProvider
    )

    fun getDrinkTypeCreateViewModel() = DrinkTypeCreateViewModel(
        iconResourceProvider = logicModule.drinkTypeIconProvider,
        drinkTypeCreator = logicModule.drinkTypeCreator,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator
    )

    fun getDrinkTypeUpdateViewModel(drinkTypeId: Int) = DrinkTypeUpdateViewModel(
        drinkTypeId = drinkTypeId,
        drinkTypeUpdater = logicModule.drinkTypeUpdater,
        drinkTypeDeleter = logicModule.drinkTypeDeleter,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        iconResourceProvider = logicModule.drinkTypeIconProvider
    )

    fun getWaterTrackCreateViewModel() = WaterTrackCreateViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackCreator = logicModule.waterTrackCreator,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator
    )

    fun getWaterTrackUpdateViewModel(waterTrackId: Int) = WaterTrackUpdateViewModel(
        waterTrackId = waterTrackId,
        heroProvider = logicModule.heroProvider,
        waterTrackUpdater = logicModule.waterTrackUpdater,
        waterTrackProvider = logicModule.waterTrackProvider,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterTrackDeleter = logicModule.waterTrackDeleter
    )

    fun getWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun getPedometerViewModel() = PedometerViewModel(
        pedometerManager = logicModule.pedometerManager,
        kilometersResolver = logicModule.kilometersResolver,
        caloriesResolver = logicModule.caloriesResolver,
        pedometerTrackProvider = logicModule.pedometerTrackProvider,
        pedometerStatisticProvider = logicModule.pedometerStatisticProvider
    )

    fun getPedometerHistoryViewModel() = PedometerHistoryViewModel(
        kilometersResolver = logicModule.kilometersResolver,
        caloriesResolver = logicModule.caloriesResolver,
        pedometerTrackProvider = logicModule.pedometerTrackProvider
    )

    fun getFastingViewModel() = FastingViewModel(
        dateTimeProvider = logicModule.dateTimeProvider,
        fastingTrackProvider = logicModule.fastingTrackProvider,
        statisticProvider = logicModule.fastingStatisticProvider,
        currentFastingManager = logicModule.currentFastingManager
    )

    fun getFastingTrackCreateViewModel() = FastingTrackCreateViewModel(
        currentFastingManager = logicModule.currentFastingManager,
        fastingPlanDurationMapper = logicModule.fastingPlanDurationResolver,
        fastingPlanProvider = logicModule.fastingPlanProvider
    )

    fun getFastingHistoryViewModel() = FastingHistoryViewModel(
        fastingTrackProvider = logicModule.fastingTrackProvider
    )

    fun getMoodTrackingViewModel() = MoodTrackingViewModel(
        moodWithActivitiesProvider = logicModule.moodWithActivitiesProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        dateTimeProvider = logicModule.dateTimeProvider,
        moodTrackingStatisticProvider = logicModule.moodTrackingStatisticProvider
    )

    fun getMoodTrackingCreateViewModel() = MoodTrackingTrackCreateViewModel(
        moodTrackCreator = logicModule.moodTrackCreator,
        moodTypeProvider = logicModule.moodTypeProvider,
        // diaryTrackCreator = logicModule.diaryTrackCreator,
        activityProvider = logicModule.activityProvider,
        moodWithActivityCreator = logicModule.moodWithActivityCreator
    )

    fun getMoodTrackingUpdateViewModel(moodTrackId: Int) = MoodTrackingTrackUpdateViewModel(
        moodTrackId = moodTrackId,
        moodTypeProvider = logicModule.moodTypeProvider,
        // diaryTrackCreator = logicModule.diaryTrackCreator,
        activityProvider = logicModule.activityProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        moodTrackUpdater = logicModule.moodTrackUpdater,
        moodWithActivityProvider = logicModule.moodWithActivitiesProvider,
        moodTrackDeleter = logicModule.moodTrackDeleter
    )

    fun getMoodTrackingHistoryViewModel() = MoodTrackingHistoryViewModel(
        moodTrackDeleter = logicModule.moodTrackDeleter,
        moodWithActivitiesProvider = logicModule.moodWithActivitiesProvider
    )

    fun getMoodTypeManageTracksViewModel() = MoodTypeManageTracksViewModel(
        moodTypeProvider = logicModule.moodTypeProvider
    )

    fun getMoodTypeTrackCreateViewModel() = MoodTypeCreateViewModel(
        moodTypeCreator = logicModule.moodTypeCreator,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        iconResourceProvider = logicModule.moodTypeIconProvider
    )

    fun getMoodTypeTrackUpdateViewModel(moodTypeId: Int) = MoodTypeUpdateViewModel(
        moodTypeId = moodTypeId,
        iconResourceProvider = logicModule.moodTypeIconProvider,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        moodTypeProvider = logicModule.moodTypeProvider,
        moodTypeUpdater = logicModule.moodTypeUpdater,
        moodTypeDeleter = logicModule.moodTypeDeleter
    )

    fun getManageActivitiesViewModel() = ManageActivitiesViewModel(
        activityProvider = logicModule.activityProvider
    )

    fun getCreateActivityViewModel() = CreateActivityViewModel(
        activityCreator = logicModule.activityCreator,
        activityNameValidator = logicModule.activityNameValidator,
        iconResourceProvider = logicModule.activityIconProvider
    )

    fun getUpdateActivityViewModel(hobbyTrackId: Int) = UpdateActivityViewModel(
        activityId = hobbyTrackId,
        activityNameValidator = logicModule.activityNameValidator,
        activityDeleter = logicModule.activityDeleter,
        activityUpdater = logicModule.activityUpdater,
        activityProvider = logicModule.activityProvider,
        iconResourceProvider = logicModule.activityIconProvider
    )
}