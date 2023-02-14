package hardcoder.dev.healther.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterTrack: WaterTrack)

    @Update
    suspend fun update(waterTrack: WaterTrack)

    @Delete
    suspend fun delete(waterTrack: WaterTrack)

    @Query("SELECT * FROM waterTracks WHERE time BETWEEN :startTime AND :endTime")
    fun getWaterTracksByDayRange(startTime: Long, endTime: Long): Flow<List<WaterTrack>>

    @Query("SELECT * FROM waterTracks WHERE id == :id")
    fun getWaterTrackById(id: Int): Flow<WaterTrack>
}