package hardcoder.dev.entities.features.moodTracking

data class MoodWithActivities(
    val moodTrack: MoodTrack,
    val moodActivityList: List<MoodActivity>,
)