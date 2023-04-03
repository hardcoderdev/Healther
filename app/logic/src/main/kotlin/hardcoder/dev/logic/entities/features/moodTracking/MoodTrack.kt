package hardcoder.dev.logic.entities.features.moodTracking

import kotlinx.datetime.Instant

data class MoodTrack(
    val id: Int,
    val moodType: MoodType,
    val date: Instant
)
