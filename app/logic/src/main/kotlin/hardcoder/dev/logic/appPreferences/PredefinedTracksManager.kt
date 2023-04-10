package hardcoder.dev.logic.appPreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTagCreator
import hardcoder.dev.logic.dataStore.healtherDataStore
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeCreator
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypeCreator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PredefinedTracksManager(
    private val context: Context,
    private val drinkTypeCreator: DrinkTypeCreator,
    private val moodTypeCreator: MoodTypeCreator,
    private val featureTagCreator: FeatureTagCreator
) {

    private val isDrinkTypeSavedPreferenceKey = booleanPreferencesKey(IS_DRINK_TYPES_SAVED)
    private val isMoodTypesSavedPreferenceKey = booleanPreferencesKey(IS_MOOD_TYPES_SAVED)
    private val isDiaryFeatureTagsSavedPreferenceKey =
        booleanPreferencesKey(IS_DIARY_FEATURE_TAGS_SAVED)

    private val isMoodTypesSaved = context.healtherDataStore.data.map {
        it[isMoodTypesSavedPreferenceKey] ?: false
    }
    private val isDrinkTypeSaved = context.healtherDataStore.data.map {
        it[isDrinkTypeSavedPreferenceKey] ?: false
    }
    private val isDiaryFeatureTagsSaved = context.healtherDataStore.data.map {
        it[isDiaryFeatureTagsSavedPreferenceKey] ?: false
    }

    suspend fun createPredefinedTracksIfNeeded() {
        if (!isDrinkTypeSaved.first()) createPredefinedDrinkTypes()
        if (!isMoodTypesSaved.first()) createPredefinedMoodTypes()
        if (!isDiaryFeatureTagsSaved.first()) createPredefinedDiaryFeatureTags()
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

    private suspend fun createPredefinedDiaryFeatureTags() {
        featureTagCreator.createPredefined()
        context.healtherDataStore.edit { predefinedData ->
            predefinedData[isDiaryFeatureTagsSavedPreferenceKey] = true
        }
    }

    private companion object {
        const val IS_DRINK_TYPES_SAVED = "predefined_isDrinkTypesSaved"
        const val IS_MOOD_TYPES_SAVED = "predefined_isMoodTypesSaved"
        const val IS_DIARY_FEATURE_TAGS_SAVED = "predefined_isDiaryFeatureTagsSaved"
    }
}