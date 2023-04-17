package hardcoder.dev.logic.appPreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import hardcoder.dev.logic.dataStore.healtherDataStore
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PredefinedTracksManager(
    private val context: Context,
    private val drinkTypeCreator: DrinkTypeCreator,
    private val moodTypeCreator: MoodTypeCreator
) {

    private val isDrinkTypeSavedPreferenceKey = booleanPreferencesKey(IS_DRINK_TYPES_SAVED)
    private val isMoodTypesSavedPreferenceKey = booleanPreferencesKey(IS_MOOD_TYPES_SAVED)

    private val isMoodTypesSaved = context.healtherDataStore.data.map {
        it[isMoodTypesSavedPreferenceKey] ?: false
    }
    private val isDrinkTypeSaved = context.healtherDataStore.data.map {
        it[isDrinkTypeSavedPreferenceKey] ?: false
    }

    suspend fun createPredefinedTracksIfNeeded() {
        if (!isDrinkTypeSaved.first()) createPredefinedDrinkTypes()
        if (!isMoodTypesSaved.first()) createPredefinedMoodTypes()
    }

    private suspend fun createPredefinedDrinkTypes() {
        drinkTypeCreator.createPredefined()
        context.healtherDataStore.edit { predefinedData ->
            predefinedData[isDrinkTypeSavedPreferenceKey] = true
        }
    }

    private suspend fun createPredefinedMoodTypes() {
        moodTypeCreator.createPredefined()
        context.healtherDataStore.edit { predefinedData ->
            predefinedData[isMoodTypesSavedPreferenceKey] = true
        }
    }

    private companion object {
        const val IS_DRINK_TYPES_SAVED = "predefined_isDrinkTypesSaved"
        const val IS_MOOD_TYPES_SAVED = "predefined_isMoodTypesSaved"
    }
}