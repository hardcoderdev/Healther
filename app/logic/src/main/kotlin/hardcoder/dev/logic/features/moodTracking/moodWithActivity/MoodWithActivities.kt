package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivity
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack

data class MoodWithActivities(
    val moodTrack: MoodTrack,
    val moodActivityList: List<MoodActivity>,
)