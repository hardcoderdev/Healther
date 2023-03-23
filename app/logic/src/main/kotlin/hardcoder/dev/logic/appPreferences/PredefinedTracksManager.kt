package hardcoder.dev.logic.appPreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import hardcoder.dev.logic.dataStore.healtherDataStore
import hardcoder.dev.logic.features.waterBalance.drinkType.DrinkTypeCreator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PredefinedTracksManager(
    private val context: Context,
    private val drinkTypeCreator: DrinkTypeCreator
) {

    private val isDrinkTypeSavedPreferenceKey = booleanPreferencesKey(IS_DRINK_TYPES_SAVED)
    private val isDrinkTypeSaved = context.healtherDataStore.data.map {
        it[isDrinkTypeSavedPreferenceKey] ?: false
    }

    suspend fun createPredefinedTracksIfNeeded() {
        when {
            isDrinkTypeSaved.first().not() -> createPredefinedDrinkTypes()
        }
    }

    private suspend fun createPredefinedDrinkTypes() {
        drinkTypeCreator.createPredefined()
        context.healtherDataStore.edit { predefinedData ->
            predefinedData[isDrinkTypeSavedPreferenceKey] = true
        }
    }

    private companion object {
        const val IS_DRINK_TYPES_SAVED = "predefined_isDrinkTypesSaved"
    }
}