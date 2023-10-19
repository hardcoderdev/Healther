package hardcoder.dev.database.dao.features.waterTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterTrack: WaterTrack)

    @Update
    suspend fun update(waterTrack: WaterTrack)

    @Query("DELETE FROM water_tracks WHERE id = :waterTrackId")
    suspend fun deleteById(waterTrackId: Int)

    @Query("DELETE FROM water_tracks WHERE drinkTypeId = :drinkTypeId")
    suspend fun deleteAllTracksByDrinkTypeId(drinkTypeId: Int)

    @Query("SELECT * FROM water_tracks WHERE id = :waterTrackId")
    fun provideWaterTrackById(waterTrackId: Int): Flow<WaterTrack>

    @Query("SELECT * FROM water_tracks WHERE creationInstant BETWEEN :startDate AND :endDate")
    fun provideWaterTracksByDayRange(
        startDate: Int,
        endDate: Int,
    ): Flow<List<WaterTrack>>

    @Query("SELECT * FROM water_tracks")
    fun provideAllWaterTracks(): Flow<List<WaterTrack>>
}