package hardcoder.dev.database.dao.features.moodTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.moodTracking.MoodType
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodType: MoodType)

    @Update
    suspend fun update(moodType: MoodType)

    @Query("DELETE FROM mood_types WHERE id = :moodTypeId")
    suspend fun deleteById(moodTypeId: Int)

    @Query("SELECT * FROM mood_types")
    fun provideAllMoodTypes(): Flow<List<MoodType>>

    @Query("SELECT * FROM mood_types WHERE id = :moodTypeId")
    fun provideMoodTypeById(moodTypeId: Int): Flow<MoodType>
}