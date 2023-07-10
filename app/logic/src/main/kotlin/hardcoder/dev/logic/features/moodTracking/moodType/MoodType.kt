package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.logic.icons.LocalIcon

data class MoodType(
    val id: Int,
    val name: String,
    val icon: LocalIcon,
    val positivePercentage: Int,
)