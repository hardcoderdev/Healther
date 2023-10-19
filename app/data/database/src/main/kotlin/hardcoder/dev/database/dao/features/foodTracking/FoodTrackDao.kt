package hardcoder.dev.database.dao.features.foodTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.foodTracking.FoodTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface FoodTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodTrack: FoodTrack)

    @Update
    suspend fun update(foodTrack: FoodTrack)

    @Query("DELETE FROM food_tracks WHERE id = :foodTrackId")
    suspend fun deleteById(foodTrackId: Int)

    @Query("DELETE FROM food_tracks WHERE foodTypeId = :foodTypeId")
    suspend fun deleteAllTracksByFoodTypeId(foodTypeId: Int)

    @Query("SELECT * FROM food_tracks WHERE id = :foodTrackId")
    fun provideFoodTrackById(foodTrackId: Int): Flow<FoodTrack>

    @Query("SELECT * FROM food_tracks WHERE creationInstant BETWEEN :startTime AND :endTime")
    fun provideFoodTracksByDayRange(
        startTime: Instant,
        endTime: Instant,
    ): Flow<List<FoodTrack>>

    @Query("SELECT * FROM food_tracks ORDER BY id DESC LIMIT 1")
    fun provideLastFoodTrack(): Flow<FoodTrack>
}