package hardcoder.dev.database.dao.features.moodTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.moodTracking.MoodTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface MoodTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodTrack: MoodTrack)

    @Update
    suspend fun update(moodTrack: MoodTrack)

    @Query("DELETE FROM mood_tracks WHERE id = :moodTrackId")
    suspend fun deleteById(moodTrackId: Int)

    @Query("DELETE FROM mood_tracks WHERE moodTypeId = :moodTypeId")
    suspend fun deleteAllTracksByMoodTypeId(moodTypeId: Int)

    @Query("SELECT * FROM mood_tracks")
    fun provideAllMoodTracks(): Flow<MoodTrack>

    @Query("SELECT * FROM mood_tracks WHERE id = :moodTrackId")
    fun provideMoodTrackById(moodTrackId: Int): Flow<MoodTrack>

    @Query("SELECT * FROM mood_tracks WHERE creationDate BETWEEN :startTime AND :endTime")
    fun provideMoodTracksByDayRange(
        startTime: Instant,
        endTime: Instant,
    ): Flow<List<MoodTrack>>
}