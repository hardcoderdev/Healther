package hardcoder.dev.presentation.features.waterBalance.drinkType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DrinkTypeManageTracksViewModel(drinkTypeProvider: DrinkTypeProvider) : ViewModel() {

    val state = drinkTypeProvider.provideAllDrinkTypes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    ).map { drinkTypeList ->
        State(drinkTypeList = drinkTypeList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            drinkTypeList = emptyList()
        )
    )

    data class State(val drinkTypeList: List<DrinkType>)
}