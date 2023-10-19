package hardcoder.dev.database.entities.features.diary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_tags")
data class DiaryTag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "diary_tag_id")
    val id: Int = 0,
    val name: String,
    val iconId: Int,
)
