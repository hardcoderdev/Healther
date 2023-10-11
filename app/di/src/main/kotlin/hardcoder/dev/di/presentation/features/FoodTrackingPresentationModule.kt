package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.foodTracking.FoodTrackingCreationViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingUpdateViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeCreationViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypesViewModel
import org.koin.dsl.module

internal val foodTrackingPresentationModule = module {
    factory {
        FoodTrackingViewModel(
            foodTrackProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        FoodTrackingCreationViewModel(
            foodTrackCreator = get(),
            foodTypeProvider = get(),
            dateTimeProvider = get(),
        )
    }

    factory { parameters ->
        FoodTrackingUpdateViewModel(
            foodTrackId = parameters.get(),
            foodTypeProvider = get(),
            foodTrackProvider = get(),
            foodTrackDeleter = get(),
            foodTrackUpdater = get(),
            dateTimeProvider = get(),
        )
    }

    factory {
        FoodTypesViewModel(
            foodTypeProvider = get(),
        )
    }

    factory {
        FoodTypeCreationViewModel(
            foodTypeCreator = get(),
            foodTypeNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    factory { parameters ->
        FoodTypeUpdateViewModel(
            foodTypeId = parameters.get(),
            foodTypeNameValidator = get(),
            foodTypeProvider = get(),
            foodTypeDeleter = get(),
            foodTypeUpdater = get(),
            iconResourceProvider = get(),
        )
    }

    factory {
        FoodTrackingHistoryViewModel(
            dateTimeProvider = get(),
            foodTrackProvider = get(),
        )
    }
}