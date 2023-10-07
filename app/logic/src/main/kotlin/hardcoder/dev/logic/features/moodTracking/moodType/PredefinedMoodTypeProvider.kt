package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.icons.Icon

interface PredefinedMoodTypeProvider {
    fun providePredefined(): List<MoodTypePredefined>
}

data class MoodTypePredefined(
    val name: String,
    val icon: Icon,
    val positivePercentage: Int,
)