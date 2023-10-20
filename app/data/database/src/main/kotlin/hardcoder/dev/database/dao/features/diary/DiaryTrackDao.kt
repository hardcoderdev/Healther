package hardcoder.dev.database.dao.features.diary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.diary.DiaryTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface DiaryTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diaryTrack: DiaryTrack): Long

    @Update
    suspend fun update(diaryTrack: DiaryTrack)

    @Query("DELETE FROM diary_tracks WHERE diary_track_id = :diaryTrackId")
    suspend fun deleteById(diaryTrackId: Int)

    @Query("SELECT * FROM diary_tracks WHERE diary_track_id = :diaryTrackId")
    fun provideDiaryTrackById(diaryTrackId: Int): Flow<List<DiaryTrack>>

    @Query("SELECT * FROM diary_tracks WHERE creationInstant BETWEEN :startTime AND :endTime")
    fun provideAllDiaryTracksByDateRange(
        startTime: Instant,
        endTime: Instant,
    ): Flow<List<DiaryTrack>>
}