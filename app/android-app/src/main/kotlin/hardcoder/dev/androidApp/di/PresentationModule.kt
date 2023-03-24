package hardcoder.dev.androidApp.di

import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.presentation.features.starvation.StarvationCreateTrackViewModel
import hardcoder.dev.presentation.features.starvation.StarvationHistoryViewModel
import hardcoder.dev.presentation.features.starvation.StarvationViewModel
import hardcoder.dev.presentation.features.waterBalance.CreateDrinkTypeViewModel
import hardcoder.dev.presentation.features.waterBalance.SaveWaterTrackViewModel
import hardcoder.dev.presentation.features.waterBalance.UpdateWaterTrackViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingViewModel
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.EnterWeightViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.presentation.setUpFlow.SplashViewModel

class PresentationModule(val logicModule: LogicModule) {

    fun createSplashViewModel() = SplashViewModel(
        appPreferenceProvider = logicModule.appPreferenceProvider
    )

    fun createEnterWeightViewModel() = EnterWeightViewModel()

    fun createSelectGenderViewModel() = SelectGenderViewModel(
        genderProvider = logicModule.genderProvider
    )

    fun createEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel()

    fun createHeroCreateViewModel() = HeroCreateViewModel(
        heroCreator = logicModule.heroCreator,
        appPreferenceUpdater = logicModule.appPreferenceUpdater
    )

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createDrinkTypeViewModel() = CreateDrinkTypeViewModel(
        drinkTypeCreator = logicModule.drinkTypeCreator,
        nameValidator = logicModule.nameValidator,
        iconResourceValidator = logicModule.iconResourceValidator
    )

    fun createSaveWaterTrackViewModel() = SaveWaterTrackViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackCreator = logicModule.waterTrackCreator,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator
    )

    fun createUpdateWaterTrackViewModel(waterTrackId: Int) = UpdateWaterTrackViewModel(
        waterTrackId = waterTrackId,
        heroProvider = logicModule.heroProvider,
        waterTrackUpdater = logicModule.waterTrackUpdater,
        waterTrackProvider = logicModule.waterTrackProvider,
        drinkTypeProvider = logicModule.drinkTypeProvider,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterTrackDeleter = logicModule.waterTrackDeleter
    )

    fun createWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createPedometerViewModel() = PedometerViewModel(
        pedometerManager = logicModule.pedometerManager,
        kilometersResolver = logicModule.kilometersResolver,
        caloriesResolver = logicModule.caloriesResolver,
        pedometerTrackProvider = logicModule.pedometerTrackProvider
    )

    fun createPedometerHistoryViewModel() = PedometerHistoryViewModel(
        kilometersResolver = logicModule.kilometersResolver,
        caloriesResolver = logicModule.caloriesResolver,
        pedometerTrackProvider = logicModule.pedometerTrackProvider
    )

    fun createStarvationViewModel() = StarvationViewModel(
        dateTimeProvider = logicModule.dateTimeProvider,
        starvationTrackProvider = logicModule.starvationTrackProvider,
        statisticProvider = logicModule.starvationStatisticProvider,
        currentStarvationManager = logicModule.currentStarvationManager
    )

    fun createStarvationCreateTrackViewModel() = StarvationCreateTrackViewModel(
        currentStarvationManager = logicModule.currentStarvationManager,
        starvationPlanDurationMapper = logicModule.starvationPlanDurationResolver,
        starvationPlanProvider = logicModule.starvationPlanProvider
    )

    fun createStarvationHistoryViewModel() = StarvationHistoryViewModel(
        starvationTrackProvider = logicModule.starvationTrackProvider
    )
}