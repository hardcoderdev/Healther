package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingHistoryViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreationViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeUpdateViewModel
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import org.koin.dsl.module

internal val waterTrackingPresentationModule = module {
    factory {
        WaterTrackingViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            millilitersDrunkProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        WaterTrackingAnalyticsViewModel(
            waterTrackingStatisticProvider = get(),
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        WaterTrackingCreationViewModel(
            waterTrackCreator = get(),
            drinkTypeProvider = get(),
            waterTrackMillilitersValidator = get(),
            dateTimeProvider = get(),
            waterTrackingDailyRateProvider = get(),
        )
    }

    factory { parameters ->
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

    factory {
        WaterTrackingHistoryViewModel(
            waterTrackProvider = get(),
            waterPercentageResolver = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        DrinkTypesViewModel(
            drinkTypeProvider = get(),
        )
    }

    factory {
        DrinkTypeCreationViewModel(
            iconResourceProvider = get(),
            drinkTypeCreator = get(),
            drinkTypeNameValidator = get(),
        )
    }

    factory { parameters ->
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