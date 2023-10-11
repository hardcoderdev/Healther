package hardcoder.dev.logics.appPreferences

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.boolean
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logics.features.waterTracking.drinkType.DrinkTypeCreator

class PredefinedTracksManager(
    private val context: Context,
    private val settings: Settings = SharedPreferencesSettings(
        // TODO KMM
        delegate = context.getSharedPreferences("MAIN_PREFS", 0),
    ),
    private val drinkTypeCreator: DrinkTypeCreator,
    private val moodTypeCreator: MoodTypeCreator,
) {

    private val isDrinkTypeSaved by settings.boolean(IS_DRINK_TYPES_SAVED, false)
    private val isMoodTypesSaved by settings.boolean(IS_MOOD_TYPES_SAVED, false)

    suspend fun createPredefinedTracksIfNeeded() {
        if (!isDrinkTypeSaved) createPredefinedDrinkTypes()
        if (!isMoodTypesSaved) createPredefinedMoodTypes()
    }

    private suspend fun createPredefinedDrinkTypes() {
        drinkTypeCreator.createPredefined()
        settings.putBoolean(IS_DRINK_TYPES_SAVED, true)
    }

    private suspend fun createPredefinedMoodTypes() {
        moodTypeCreator.createPredefined()
        settings.putBoolean(IS_MOOD_TYPES_SAVED, true)
    }

    private companion object {
        const val IS_DRINK_TYPES_SAVED = "predefined_isDrinkTypesSaved"
        const val IS_MOOD_TYPES_SAVED = "predefined_isMoodTypesSaved"
    }
}