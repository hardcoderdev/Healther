package hardcoder.dev.database.dao.features.moodTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.moodTracking.MoodActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodActivity: MoodActivity)

    @Update
    suspend fun update(moodActivity: MoodActivity)

    @Query("DELETE FROM mood_activities WHERE mood_activity_id = :moodActivityId")
    suspend fun deleteById(moodActivityId: Int)

    @Query("SELECT * FROM mood_activities")
    fun provideAllMoodActivities(): Flow<List<MoodActivity>>

    @Query("SELECT * FROM mood_activities WHERE mood_activity_id = :moodActivityId")
    fun provideMoodActivityById(moodActivityId: Int): Flow<MoodActivity?>
}