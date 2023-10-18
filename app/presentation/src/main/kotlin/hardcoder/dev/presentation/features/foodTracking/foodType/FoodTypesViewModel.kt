package hardcoder.dev.presentation.features.foodTracking.foodType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.foodTracking.foodType.FoodTypeProvider

class FoodTypesViewModel(
    foodTypeProvider: FoodTypeProvider,
) : ViewModel() {

    val foodTypesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = foodTypeProvider.provideAllFoodTypes(),
    )
}