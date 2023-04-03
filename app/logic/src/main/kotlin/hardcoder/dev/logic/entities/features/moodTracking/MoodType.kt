package hardcoder.dev.logic.entities.features.moodTracking

import hardcoder.dev.logic.icons.LocalIcon

data class MoodType(
    val id: Int,
    val name: String,
    val icon: LocalIcon,
    val positivePercentage: Int
)