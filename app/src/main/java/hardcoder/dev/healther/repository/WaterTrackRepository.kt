package hardcoder.dev.healther.repository

import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.WaterTrackDao
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.ui.base.composables.Drink
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class WaterTrackRepository(
    private val waterTrackDao: WaterTrackDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun insert(waterTrack: WaterTrack) {
        withContext(dispatcher) {
            waterTrackDao.insert(waterTrack)
        }
    }

    suspend fun update(waterTrack: WaterTrack) {
        withContext(dispatcher) {
            waterTrackDao.update(waterTrack)
        }
    }

    suspend fun delete(waterTrackId: Int) {
        withContext(dispatcher) {
            waterTrackDao.delete(waterTrackId)
        }
    }

    fun getWaterTracksByDayRange(dayRange: LongRange): Flow<List<WaterTrack>> {
        return waterTrackDao.getWaterTracksByDayRange(dayRange.first, dayRange.last)
    }

    fun getWaterTrackById(id: Int): Flow<WaterTrack?> {
        return waterTrackDao.getWaterTrackById(id)
    }

    fun getAllDrinkTypes(): Flow<List<Drink>> {
        return flow {
            emit(
                listOf(
                    Drink(type = DrinkType.WATER, image = R.drawable.water),
                    Drink(type = DrinkType.TEA, image = R.drawable.tea),
                    Drink(type = DrinkType.COFFEE, image = R.drawable.coffee),
                    Drink(type = DrinkType.BEER, image = R.drawable.beer),
                    Drink(type = DrinkType.JUICE, image = R.drawable.juice),
                    Drink(type = DrinkType.SODA, image = R.drawable.soda),
                    Drink(type = DrinkType.SOUP, image = R.drawable.soup),
                    Drink(type = DrinkType.MILK, image = R.drawable.milk)
                )
            )
        }
    }
}