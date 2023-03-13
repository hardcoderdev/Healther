package hardcoder.dev.logic.features.waterBalance

import hardcoder.dev.entities.waterTracking.DrinkType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DrinkTypeProvider {

    fun getAllDrinkTypes(): Flow<List<DrinkType>> {
        return flowOf(DrinkType.values().toList())
    }
}