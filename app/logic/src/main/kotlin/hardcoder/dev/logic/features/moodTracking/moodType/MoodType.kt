package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.icons.Icon

data class MoodType(
    val id: Int,
    val name: String,
    val icon: Icon,
    val positivePercentage: Int,
)