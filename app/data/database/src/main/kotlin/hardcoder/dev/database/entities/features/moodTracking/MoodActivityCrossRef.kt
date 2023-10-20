package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["mood_track_id", "mood_activity_id"])
data class MoodActivityCrossRef(
    @ColumnInfo(name = "mood_track_id")
    val moodTrackId: Int,
    @ColumnInfo(name = "mood_activity_id")
    val moodActivityId: Int,
)
