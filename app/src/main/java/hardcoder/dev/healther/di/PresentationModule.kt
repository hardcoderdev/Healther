package hardcoder.dev.healther.di

import hardcoder.dev.healther.logic.DrinkTypeImageResolver
import hardcoder.dev.healther.ui.screens.splash.SplashViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress.EnterExerciseStressTimeViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.gender.SelectGenderViewModel
import hardcoder.dev.healther.ui.screens.setUpFlow.weight.EnterWeightViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.history.WaterTrackingHistoryViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.update.UpdateWaterTrackViewModel

class PresentationModule(
    private val logicModule: LogicModule,
    private val repositoryModule: RepositoryModule
) {
    val drinkTypeImageResolver by lazy {
        DrinkTypeImageResolver()
    }

    fun createSplashViewModel() = SplashViewModel(
        userRepository = repositoryModule.userRepository
    )

    fun createEnterWeightViewModel() =  EnterWeightViewModel(
        userRepository = repositoryModule.userRepository
    )

    fun createSelectGenderViewModel() = SelectGenderViewModel(
        userRepository = repositoryModule.userRepository
    )

    fun createEnterExerciseStressTimeViewModel() = EnterExerciseStressTimeViewModel(
        userRepository = repositoryModule.userRepository
    )

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository,
        userRepository = repositoryModule.userRepository,
        waterIntakeResolver = logicModule.waterIntakeResolver
    )

    fun createSaveWaterTrackViewModel() = SaveWaterTrackViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createUpdateWaterTrackViewModel(waterTrackId: Int) = UpdateWaterTrackViewModel(
        waterTrackId = waterTrackId,
        waterTrackRepository = repositoryModule.waterTrackRepository,
        drinkTypeImageResolver = drinkTypeImageResolver
    )

    fun createWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository
    )
}