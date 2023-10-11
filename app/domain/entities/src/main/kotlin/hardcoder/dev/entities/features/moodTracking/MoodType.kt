package hardcoder.dev.entities.features.moodTracking

import hardcoder.dev.icons.Icon

data class MoodType(
    val id: Int,
    val name: String,
    val icon: Icon,
    val positivePercentage: Int,
)