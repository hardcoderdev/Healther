package hardcoder.dev.logics.features.moodTracking.moodType

import hardcoder.dev.entities.features.moodTracking.MoodTypePredefined

interface PredefinedMoodTypeProvider {
    fun providePredefined(): List<MoodTypePredefined>
}