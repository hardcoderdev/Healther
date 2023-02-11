package hardcoder.dev.healther.di

import hardcoder.dev.healther.ui.UserViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.history.WaterTrackingHistoryViewModel

class PresentationModule(
    private val logicModule: LogicModule,
    private val repositoryModule: RepositoryModule
) {

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository,
        waterIntakeResolver = logicModule.waterIntakeResolver
    )

    fun createSaveWaterTrackViewModel() = SaveWaterTrackViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createWaterTrackingHistoryViewModel() = WaterTrackingHistoryViewModel(
        waterTrackRepository = repositoryModule.waterTrackRepository
    )

    fun createUserViewModel() = UserViewModel(
        userRepository = repositoryModule.userRepository
    )
}