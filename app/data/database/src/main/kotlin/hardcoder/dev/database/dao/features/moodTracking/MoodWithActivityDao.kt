package hardcoder.dev.database.dao.features.moodTracking

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hardcoder.dev.database.entities.features.moodTracking.MoodActivityCrossRef
import hardcoder.dev.database.entities.features.moodTracking.MoodTrackWithActivities
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodWithActivityDao {

    @Upsert
    suspend fun upsert(moodActivityCrossRef: MoodActivityCrossRef)

    @Transaction
    @Query("SELECT * FROM mood_tracks")
    fun getMoodTracksWithActivities(): Flow<List<MoodTrackWithActivities>>
}