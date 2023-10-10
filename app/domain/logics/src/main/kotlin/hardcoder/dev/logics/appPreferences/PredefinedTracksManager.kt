package hardcoder.dev.logics.appPreferences

import android.content.Context
import androidx.core.content.edit
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeCreator

class PredefinedTracksManager(
    private val context: Context,
    private val drinkTypeCreator: DrinkTypeCreator,
    private val moodTypeCreator: MoodTypeCreator,
) {

    private val sharedPreferences by lazy { context.getSharedPreferences("MAIN_PREFS", 0) }
    private val isDrinkTypeSaved by lazy { sharedPreferences.getBoolean(IS_DRINK_TYPES_SAVED, false) }
    private val isMoodTypesSaved by lazy { sharedPreferences.getBoolean(IS_MOOD_TYPES_SAVED, false) }

    suspend fun createPredefinedTracksIfNeeded() {
        if (!isDrinkTypeSaved) createPredefinedDrinkTypes()
        if (!isMoodTypesSaved) createPredefinedMoodTypes()
    }

    private suspend fun createPredefinedDrinkTypes() {
        drinkTypeCreator.createPredefined()
        sharedPreferences.edit {
            putBoolean(IS_DRINK_TYPES_SAVED, true)
        }
    }

    private suspend fun createPredefinedMoodTypes() {
        moodTypeCreator.createPredefined()
        sharedPreferences.edit {
            putBoolean(IS_MOOD_TYPES_SAVED, true)
        }
    }

    private companion object {
        const val IS_DRINK_TYPES_SAVED = "predefined_isDrinkTypesSaved"
        const val IS_MOOD_TYPES_SAVED = "predefined_isMoodTypesSaved"
    }
}