package hardcoder.dev.logic.features.moodTracking.moodWithActivity

import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack

data class MoodWithActivities(
    val moodTrack: MoodTrack,
    val activityList: List<Activity>
)