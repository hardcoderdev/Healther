package hardcoder.dev.logic.features.moodTracking.moodType

interface PredefinedMoodTypeProvider {
    fun providePredefined(): List<MoodTypePredefined>
}

data class MoodTypePredefined(
    val name: String,
    val icon: hardcoder.dev.icons.Icon,
    val positivePercentage: Int,
)