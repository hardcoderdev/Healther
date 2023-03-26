package hardcoder.dev.androidApp.di

import hardcoder.dev.presentation.features.fasting.FastingHistoryViewModel
import hardcoder.dev.presentation.features.fasting.FastingTrackCreateViewModel
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackCreateViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackUpdateViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeCreateViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterWeightViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.presentation.setUpFlow.SplashViewModel

class PresentationModule(val logicModule: LogicModule) {

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
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun getDrinkTypeViewModel() = DrinkTypeViewModel(
        drinkTypeProvider = logicModule.drinkTypeProvider
    )

    fun getDrinkTypeCreateViewModel() = DrinkTypeCreateViewModel(
        iconResourceProvider = logicModule.iconResourceProvider,
        drinkTypeCreator = logicModule.drinkTypeCreator,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        drinkTypeIconResourceValidator = logicModule.drinkTypeIconResourceValidator
    )

    fun getDrinkTypeUpdateViewModel(drinkTypeId: Int) = DrinkTypeUpdateViewModel(
        drinkTypeId = drinkTypeId,
        drinkTypeUpdater = logicModule.drinkTypeUpdater,
        drinkTypeDeleter = logicModule.drinkTypeDeleter,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        drinkTypeNameValidator = logicModule.drinkTypeNameValidator,
        drinkTypeIconResourceValidator = logicModule.drinkTypeIconResourceValidator,
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
        pedometerTrackProvider = logicModule.pedometerTrackProvider
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
}