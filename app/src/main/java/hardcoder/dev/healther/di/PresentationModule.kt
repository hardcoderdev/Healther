package hardcoder.dev.healther.di

import hardcoder.dev.healther.ui.UserViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingViewModel
import hardcoder.dev.healther.ui.screens.waterTracking.create.SaveWaterTrackViewModel

class PresentationModule(
    private val databaseModule: DatabaseModule,
    private val logicModule: LogicModule,
    private val repositoryModule: RepositoryModule
) {

    fun createWaterTrackingViewModel() = WaterTrackingViewModel(
        waterTrackDao = databaseModule.waterTrackDao,
        waterPercentageResolver = logicModule.waterPercentageResolver,
        waterIntakeResolver = logicModule.waterIntakeResolver
    )

    fun createSaveWaterTrackViewModel() = SaveWaterTrackViewModel(
        waterTrackDao = databaseModule.waterTrackDao,
        waterPercentageResolver = logicModule.waterPercentageResolver
    )

    fun createUserViewModel() = UserViewModel(
        userRepository = repositoryModule.userRepository
    )
}