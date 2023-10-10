package hardcoder.dev.presentation.features.waterTracking.drinkType

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider
import hardcoder.dev.viewmodel.ViewModel

class DrinkTypesViewModel(drinkTypeProvider: DrinkTypeProvider) : ViewModel() {

    val drinkTypesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = drinkTypeProvider.provideAllDrinkTypes(),
    )
}