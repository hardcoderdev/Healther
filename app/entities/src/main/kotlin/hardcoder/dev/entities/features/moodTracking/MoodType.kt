package hardcoder.dev.entities.features.moodTracking

data class MoodType(
    val id: Int,
    val name: String,
    val iconResourceName: String,
    val positivePercentage: Int
)