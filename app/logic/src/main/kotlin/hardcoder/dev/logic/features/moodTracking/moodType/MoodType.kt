package hardcoder.dev.logic.features.moodTracking.moodType

data class MoodType(
    val id: Int,
    val name: String,
    val icon: hardcoder.dev.icons.Icon,
    val positivePercentage: Int,
)