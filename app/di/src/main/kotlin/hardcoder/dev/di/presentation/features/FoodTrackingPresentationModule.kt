package hardcoder.dev.di.presentation.features

import hardcoder.dev.presentation.features.foodTracking.FoodTrackingCreationViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingHistoryViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingUpdateViewModel
import hardcoder.dev.presentation.features.foodTracking.FoodTrackingViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeCreationViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypeUpdateViewModel
import hardcoder.dev.presentation.features.foodTracking.foodType.FoodTypesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val foodTrackingPresentationModule = module {
    viewModel {
        FoodTrackingViewModel(
            foodTrackProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        FoodTrackingCreationViewModel(
            foodTrackCreator = get(),
            foodTypeProvider = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel { parameters ->
        FoodTrackingUpdateViewModel(
            foodTrackId = parameters.get(),
            foodTypeProvider = get(),
            foodTrackProvider = get(),
            foodTrackDeleter = get(),
            foodTrackUpdater = get(),
            dateTimeProvider = get(),
        )
    }

    viewModel {
        FoodTypesViewModel(
            foodTypeProvider = get(),
        )
    }

    viewModel {
        FoodTypeCreationViewModel(
            foodTypeCreator = get(),
            foodTypeNameValidator = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel { parameters ->
        FoodTypeUpdateViewModel(
            foodTypeId = parameters.get(),
            foodTypeNameValidator = get(),
            foodTypeProvider = get(),
            foodTypeDeleter = get(),
            foodTypeUpdater = get(),
            iconResourceProvider = get(),
        )
    }

    viewModel {
        FoodTrackingHistoryViewModel(
            dateTimeProvider = get(),
            foodTrackProvider = get(),
        )
    }
}