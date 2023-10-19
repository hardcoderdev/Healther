package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "mood_tracks",
    foreignKeys = [
        ForeignKey(
            entity = MoodType::class,
            parentColumns = ["mood_type_id"],
            childColumns = ["moodTypeId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class MoodTrack(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mood_track_id")
    val id: Int = 0,
    val moodTypeId: Int,
    val creationDate: Instant,
)
