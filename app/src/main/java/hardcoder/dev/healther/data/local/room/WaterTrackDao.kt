package hardcoder.dev.healther.data.local.room

import androidx.room.Dao
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

    @Query("DELETE FROM waterTracks WHERE ID == :waterTrackId")
    suspend fun delete(waterTrackId: Int)

    @Query("SELECT * FROM waterTracks WHERE date BETWEEN :startOfDayTime AND :endOfDayTime")
    fun getWaterTracksByDayRange(startOfDayTime: Long, endOfDayTime: Long): Flow<List<WaterTrack>>

    @Query("SELECT * FROM waterTracks WHERE id == :id")
    fun getWaterTrackById(id: Int): Flow<WaterTrack?>
}