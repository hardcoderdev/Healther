package hardcoder.dev.database.dao.features.diary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.diary.DiaryTag
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diaryTag: DiaryTag)

    @Update
    suspend fun update(diaryTag: DiaryTag)

    @Query("DELETE FROM diary_tags WHERE id = :diaryTagId")
    suspend fun deleteById(diaryTagId: Int)

    @Query("SELECT * FROM diary_tags")
    fun provideAllDiaryTags(): Flow<List<DiaryTag>>

    @Query("SELECT * FROM diary_tags WHERE id = :diaryTagId")
    fun provideDiaryTagById(diaryTagId: Int): Flow<DiaryTag>
}