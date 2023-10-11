package hardcoder.dev.presentation.features.foodTracking.foodType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider

class FoodTypesViewModel(
    foodTypeProvider: FoodTypeProvider,
) : ScreenModel {

    val foodTypesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = foodTypeProvider.provideAllFoodTypes(),
    )
}