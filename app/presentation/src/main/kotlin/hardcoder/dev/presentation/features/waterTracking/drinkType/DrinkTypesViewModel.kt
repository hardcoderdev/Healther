package hardcoder.dev.presentation.features.waterTracking.drinkType

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeProvider

class DrinkTypesViewModel(drinkTypeProvider: DrinkTypeProvider) : ScreenModel {

    val drinkTypesLoadingController = LoadingController(
        coroutineScope = coroutineScope,
        flow = drinkTypeProvider.provideAllDrinkTypes(),
    )
}