package hardcoder.dev.database.entities.features.diary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_attachments")
data class DiaryAttachment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "diary_attachment_id")
    val id: Int = 0,
    val diaryTrackId: Int,
    val targetTypeId: Int,
    val targetId: Int,
)
