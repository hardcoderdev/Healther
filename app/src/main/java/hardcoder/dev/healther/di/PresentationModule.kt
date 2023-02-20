package hardcoder.dev.healther.di

import hardcoder.dev.healther.logic.resolvers.DrinkTypeImageResolver
import hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress.EnterExerciseStressTimeViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.gender.SelectGenderViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.heroCreate.HeroCreateViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.weight.EnterWeightViewModel
import hardcoder.dev.healther.ui.screens.splash.SplashViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.history.WaterTrackingHistoryViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.update.UpdateWaterTrackViewModel

class PresentationModule(
    private val logicModule: LogicModule
) {
    val drinkTypeImageResolver by lazy {
        DrinkTypeImageResolver()
    }

    fun createSplashViewModel() = SplashViewModel(
        appPreferenceProvider = logicModule.appPreferenceProvider
    )

    fun createEnterWeightViewModel() = EnterWeightViewModel()

    fun createSelectGenderViewModel() = SelectGenderViewModel()

    fun createEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel()

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        appPreferenceUpdater = logicModule.appPreferenceUpdater,
        heroProvider = logicModule.heroProvider,
        waterTrackDeleter = logicModule.waterTrackDeleter,
        waterTrackProvider = logicModule.waterTrackProvider,
        waterIntakeResolver = logicModule.waterIntakeResolver,
        waterPercentageResolver = logicModule.waterPercentageResolver,
        drinkTypeImageResolver = drinkTypeImageResolver
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
        drinkTypeImageResolver = drinkTypeImageResolver,
        waterTrackMillilitersValidator = logicModule.waterTrackMillilitersValidator,
        waterIntakeResolver = logicModule.waterIntakeResolver
    )

    fun createWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackProvider = logicModule.waterTrackProvider,
        waterTrackDeleter = logicModule.waterTrackDeleter,
        drinkTypeImageResolver = drinkTypeImageResolver,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createHeroCreateViewModel() = HeroCreateViewModel(
        heroCreator = logicModule.heroCreator,
        appPreferenceCreator = logicModule.appPreferenceCreator
    )
}