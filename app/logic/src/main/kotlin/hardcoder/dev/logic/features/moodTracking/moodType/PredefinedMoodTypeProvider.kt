package hardcoder.dev.logic.features.moodTracking.moodType

import hardcoder.dev.logic.icons.LocalIcon

interface PredefinedMoodTypeProvider {
    fun providePredefined(): List<MoodTypePredefined>
}

data class MoodTypePredefined(
    val name: String,
    val icon: LocalIcon,
    val positivePercentage: Int,
)