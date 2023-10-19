package hardcoder.dev.database.dao.features.diary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hardcoder.dev.database.entities.features.diary.DiaryAttachment
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryAttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diaryAttachment: DiaryAttachment)

    @Query("SELECT * FROM diary_attachments WHERE targetTypeId = :targetTypeId AND targetId = :targetId")
    fun provideDiaryAttachmentsByTargetId(
        targetTypeId: Int,
        targetId: Int,
    ): Flow<List<DiaryAttachment>>

    @Query("SELECT * FROM diary_attachments WHERE diaryTrackId = :diaryTrackId")
    fun provideDiaryAttachmentsByDiaryTrackId(diaryTrackId: Int): Flow<List<DiaryAttachment>>

    @Query("DELETE FROM diary_attachments WHERE diaryTrackId = :diaryTrackId")
    suspend fun deleteDiaryAttachmentsByDiaryTrackId(diaryTrackId: Int)

    @Query("DELETE FROM diary_attachments WHERE targetTypeId = :targetTypeId AND targetId = :targetId")
    suspend fun deleteDiaryAttachmentsByTargetId(
        targetTypeId: Int,
        targetId: Int,
    )
}