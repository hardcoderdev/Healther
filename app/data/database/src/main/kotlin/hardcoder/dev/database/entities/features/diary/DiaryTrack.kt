package hardcoder.dev.database.entities.features.diary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "diary_tracks")
data class DiaryTrack(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "diary_track_id")
    val id: Int = 0,
    val content: String,
    val creationInstant: Instant,
)
