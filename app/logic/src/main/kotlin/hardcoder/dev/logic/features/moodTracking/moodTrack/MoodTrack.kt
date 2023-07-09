package hardcoder.dev.logic.features.moodTracking.moodTrack

import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import kotlinx.datetime.Instant

data class MoodTrack(
    val id: Int,
    val moodType: MoodType,
    val date: Instant,
)