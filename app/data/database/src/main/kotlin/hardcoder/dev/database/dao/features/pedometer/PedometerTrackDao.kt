package hardcoder.dev.database.dao.features.pedometer

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hardcoder.dev.database.entities.features.pedometer.PedometerTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface PedometerTrackDao {

    @Upsert
    suspend fun upsert(pedometerTrack: PedometerTrack)

    @Query("SELECT * FROM pedometer_tracks")
    fun provideAllPedometerTracks(): Flow<List<PedometerTrack>>

    @Query("SELECT * FROM pedometer_tracks WHERE startTime >= :startRangeStartTime AND startTime <= :startRangeEndTime AND endTime >= :endRangeStartTime AND endTime <= :endRangeEndTime")
    fun providePedometerTracksByRange(
        startRangeStartTime: Instant,
        startRangeEndTime: Instant,
        endRangeStartTime: Instant,
        endRangeEndTime: Instant,
    ): Flow<List<PedometerTrack>>
}