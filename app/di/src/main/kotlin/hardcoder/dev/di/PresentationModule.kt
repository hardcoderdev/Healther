package hardcoder.dev.di

import hardcoder.dev.presentation.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.EnterWeightViewModel
import hardcoder.dev.presentation.HeroCreateViewModel
import hardcoder.dev.presentation.SaveWaterTrackViewModel
import hardcoder.dev.presentation.SelectGenderViewModel
import hardcoder.dev.presentation.SplashViewModel
import hardcoder.dev.presentation.UpdateWaterTrackViewModel
import hardcoder.dev.presentation.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.WaterTrackingViewModel

class PresentationModule(
    private val logicModule: LogicModule
) {
    fun createSplashViewModel() = SplashViewModel(
        appPreferenceProvider = logicModule.appPreferenceProvider
    )

    fun createEnterWeightViewModel() = EnterWeightViewModel()

    fun createSelectGenderViewModel() = SelectGenderViewModel()

    fun createEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel()

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackDeleter = logicModule.waterTrackDeleter,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createSaveWaterTrackViewModel() = SaveWaterTrackViewModel(
        heroProvider = logicModule.heroProvider,
        waterTrackCreator = logicModule.waterTrackCreator,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator
    )

    fun createUpdateWaterTrackViewModel(waterTrackId: Int) = UpdateWaterTrackViewModel(
        waterTrackId = waterTrackId,
        heroProvider = logicModule.heroProvider,
        waterTrackUpdater = logicModule.waterTrackUpdater,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        waterIntakeResolver = logicModule.waterIntakeResolver
    )

    fun createWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterTrackDeleter = logicModule.waterTrackDeleter,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createHeroCreateViewModel() = HeroCreateViewModel(
        heroCreator = logicModule.heroCreator,
        appPreferenceUpdater = logicModule.appPreferenceUpdater
    )
}