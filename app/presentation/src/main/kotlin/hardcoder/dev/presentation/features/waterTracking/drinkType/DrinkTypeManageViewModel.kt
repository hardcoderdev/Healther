package hardcoder.dev.presentation.features.waterTracking.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeProvider

class DrinkTypeManageViewModel(drinkTypeProvider: DrinkTypeProvider) : ViewModel() {

    val drinkTypesLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = drinkTypeProvider.provideAllDrinkTypes()
    )

}