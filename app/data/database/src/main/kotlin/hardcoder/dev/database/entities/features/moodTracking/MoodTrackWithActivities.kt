package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MoodTrackWithActivities(
    @Embedded val moodTrack: MoodTrack,
    @Relation(
        parentColumn = "mood_track_id",
        entityColumn = "mood_activity_id",
        associateBy = Junction(MoodActivityCrossRef::class),
    )
    val moodActivityList: List<MoodActivity>,
)
