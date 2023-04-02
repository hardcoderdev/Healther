package hardcoder.dev.androidApp.di

import android.content.Context
import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.presentation.features.fasting.FastingTrackCreateViewModel
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingTrackUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.presentation.features.moodTracking.hobby.HobbyManageTracksViewModel
import hardcoder.dev.presentation.features.moodTracking.hobby.HobbyTrackCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.hobby.HobbyTrackUpdateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreateViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeManageTracksViewModel
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackCreateViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackUpdateViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeCreateViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeManageTracksViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeUpdateViewModel
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

    fun getDrinkTypeManageTracksViewModel() = DrinkTypeManageTracksViewModel(
        drinkTypeProvider = logicModule.drinkTypeProvider
    )

    fun getDrinkTypeCreateViewModel() = DrinkTypeCreateViewModel(
        iconResourceProvider = logicModule.iconResourceProvider,
        drinkTypeCreator = logicModule.drinkTypeCreator,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        iconResourceValidator = logicModule.iconResourceValidator
    )

    fun getDrinkTypeUpdateViewModel(drinkTypeId: Int) = DrinkTypeUpdateViewModel(
        drinkTypeId = drinkTypeId,
        drinkTypeUpdater = logicModule.drinkTypeUpdater,
        drinkTypeDeleter = logicModule.drinkTypeDeleter,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        iconResourceValidator = logicModule.iconResourceValidator,
        iconResourceProvider = logicModule.iconResourceProvider
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
        moodTrackProvider = logicModule.moodTrackProvider,
        moodWithHobbyProvider = logicModule.moodWithHobbyProvider,
        hobbyTrackProvider = logicModule.hobbyTrackProvider,
        moodTrackingStatisticProvider = logicModule.moodTrackingStatisticProvider,
        dateTimeProvider = logicModule.dateTimeProvider
    )

    fun getMoodTrackingCreateViewModel() = MoodTrackingTrackCreateViewModel(
        moodTrackCreator = logicModule.moodTrackCreator,
        moodTypeProvider = logicModule.moodTypeProvider,
       // diaryTrackCreator = logicModule.diaryTrackCreator,
        hobbyTrackProvider = logicModule.hobbyTrackProvider,
        moodWithHobbyCreator = logicModule.moodWithHobbyCreator
    )

    fun getMoodTrackingUpdateViewModel(moodTrackId: Int) = MoodTrackingTrackUpdateViewModel(
        moodTrackId = moodTrackId,
        moodTypeProvider = logicModule.moodTypeProvider,
        // diaryTrackCreator = logicModule.diaryTrackCreator,
        hobbyTrackProvider = logicModule.hobbyTrackProvider,
        moodTrackProvider = logicModule.moodTrackProvider,
        moodTrackUpdater = logicModule.moodTrackUpdater,
        moodWithHobbyProvider = logicModule.moodWithHobbyProvider,
        moodWithHobbyCreator = logicModule.moodWithHobbyCreator,
        moodTrackDeleter = logicModule.moodTrackDeleter,
        moodWithHobbyDeleter = logicModule.moodWithHobbyDeleter
    )

    fun getMoodTrackingHistoryViewModel() = MoodTrackingHistoryViewModel(
        moodTrackProvider = logicModule.moodTrackProvider,
        moodTrackDeleter = logicModule.moodTrackDeleter,
        hobbyTrackProvider = logicModule.hobbyTrackProvider,
        moodWithHobbyProvider = logicModule.moodWithHobbyProvider
    )

    fun getMoodTypeManageTracksViewModel() = MoodTypeManageTracksViewModel(
        moodTypeProvider = logicModule.moodTypeProvider
    )

    fun getMoodTypeTrackCreateViewModel() = MoodTypeCreateViewModel(
        moodTypeCreator = logicModule.moodTypeCreator,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        iconResourceProvider = logicModule.iconResourceProvider,
        iconResourceValidator = logicModule.iconResourceValidator
    )

    fun getMoodTypeTrackUpdateViewModel(moodTypeId: Int) = MoodTypeUpdateViewModel(
        moodTypeId = moodTypeId,
        iconResourceProvider = logicModule.iconResourceProvider,
        iconResourceValidator = logicModule.iconResourceValidator,
        moodTypeNameValidator = logicModule.moodTypeNameValidator,
        moodTypeProvider = logicModule.moodTypeProvider,
        moodTypeUpdater = logicModule.moodTypeUpdater,
        moodTypeDeleter = logicModule.moodTypeDeleter
    )

    fun getHobbyManageTracksViewModel() = HobbyManageTracksViewModel(
        hobbyTrackProvider = logicModule.hobbyTrackProvider
    )

    fun getHobbyTrackCreateViewModel() = HobbyTrackCreateViewModel(
        hobbyTrackCreator = logicModule.hobbyTrackCreator,
        hobbyNameValidator = logicModule.hobbyNameValidator,
        hobbyIconValidator = logicModule.hobbyIconValidator,
        iconResourceProvider = logicModule.iconResourceProvider
    )

    fun getHobbyTrackUpdateViewModel(hobbyTrackId: Int) = HobbyTrackUpdateViewModel(
        hobbyTrackId = hobbyTrackId,
        hobbyNameValidator = logicModule.hobbyNameValidator,
        hobbyIconValidator = logicModule.hobbyIconValidator,
        hobbyTrackDeleter = logicModule.hobbyTrackDeleter,
        hobbyTrackUpdater = logicModule.hobbyTrackUpdater,
        hobbyTrackProvider = logicModule.hobbyTrackProvider,
        iconResourceProvider = logicModule.iconResourceProvider,
        moodTrackDeleter = logicModule.moodTrackDeleter
    )
}