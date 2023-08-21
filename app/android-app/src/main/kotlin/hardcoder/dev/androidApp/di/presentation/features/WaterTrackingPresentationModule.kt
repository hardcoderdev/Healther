package hardcoder.dev.androidApp.di.presentation.features

import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val waterTrackingPresentationModule = module {
    viewModel {
        WaterTrackingViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            millilitersDrunkProvider = get(),
            dateTimeProvider = get(),
            currencyProvider = get(),
            currencyCollector = get(),
            experienceCollector = get(),
        )
    }

    viewModel {
        WaterTrackingAnalyticsViewModel(
            waterTrackingStatisticProvider = get(),
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        WaterTrackingCreationViewModel(
            waterTrackCreator = get(),
            drinkTypeProvider = get(),
            waterTrackMillilitersValidator = get(),
            dateTimeProvider = get(),
            waterTrackingDailyRateProvider = get(),
            currencyCreator = get(),
            currencyCalculator = get(),
            experienceCreator = get(),
            experienceCalculator = get()
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
            dateTimeProvider = get(),
        )
    }

    viewModel {
        WaterTrackingHistoryViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        DrinkTypesViewModel(
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
}