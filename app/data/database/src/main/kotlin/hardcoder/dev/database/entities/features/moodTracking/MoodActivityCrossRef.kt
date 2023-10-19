package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.Entity

@Entity(primaryKeys = ["mood_track_id", "mood_activity_id"])
data class MoodActivityCrossRef(
    val moodTrackId: Int,
    val moodActivityId: Int,
)
