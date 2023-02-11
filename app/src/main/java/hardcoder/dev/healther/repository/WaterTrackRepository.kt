package hardcoder.dev.healther.repository

import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.WaterTrackDao
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.ui.screens.waterTracking.create.Drink
import kotlinx.coroutines.flow.Flow

class WaterTrackRepository(private val waterTrackDao: WaterTrackDao) {

    suspend fun insert(waterTrack: WaterTrack) {
        waterTrackDao.insert(waterTrack)
    }

    suspend fun update(waterTrack: WaterTrack) {
        waterTrackDao.update(waterTrack)
    }

    suspend fun delete(waterTrack: WaterTrack) {
        waterTrackDao.delete(waterTrack)
    }

    fun getAllWaterTracks(startTime: Long, endTime: Long): Flow<List<WaterTrack>> {
        return waterTrackDao.getWaterTracksForDay(startTime, endTime)
    }

    suspend fun getWaterTrackById(id: Int): WaterTrack {
        return waterTrackDao.getWaterTrackById(id)
    }

    fun getAllDrinkTypes(): List<Drink> {
        return listOf(
            Drink(type = DrinkType.WATER, image = R.drawable.water),
            Drink(type = DrinkType.TEA, image = R.drawable.tea),
            Drink(type = DrinkType.COFFEE, image = R.drawable.coffee),
            Drink(type = DrinkType.BEER, image = R.drawable.beer),
            Drink(type = DrinkType.JUICE, image = R.drawable.juice),
            Drink(type = DrinkType.SODA, image = R.drawable.soda),
            Drink(type = DrinkType.SOUP, image = R.drawable.soup),
            Drink(type = DrinkType.MILK, image = R.drawable.milk)
        )
    }
}