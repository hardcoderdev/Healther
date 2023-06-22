package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val waterTrackingPresentationModule = module {
    viewModel {
        WaterTrackingViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            waterTrackingStatisticProvider = get(),
            millilitersDrunkProvider = get(),
        )
    }

    viewModel {
        DrinkTypeViewModel(
            drinkTypeProvider = get(),
        )
    }

    viewModel {
        DrinkTypeCreationViewModel(
            iconResourceProvider = get(),
            drinkTypeCreator = get(),
            drinkTypeNameValidator = get(),
        )
    }

    viewModel { parameters ->
        DrinkTypeUpdateViewModel(
            drinkTypeId = parameters.get(),
            drinkTypeUpdater = get(),
            drinkTypeDeleter = get(),
            drinkTypeProvider = get(),
            drinkTypeNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel {
        WaterTrackingCreationViewModel(
            waterTrackCreator = get(),
            drinkTypeProvider = get(),
            waterTrackMillilitersValidator = get(),
            dateTimeProvider = get(),
            waterTrackingDailyRateProvider = get(),
        )
    }

    viewModel { parameters ->
        WaterTrackingUpdateViewModel(
            waterTrackId = parameters.get(),
            waterTrackUpdater = get(),
            waterTrackProvider = get(),
            drinkTypeProvider = get(),
            waterTrackMillilitersValidator = get(),
            waterTrackDeleter = get(),
            waterTrackingDailyRateProvider = get(),
        )
    }

    viewModel {
        WaterTrackingHistoryViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
        )
    }
}