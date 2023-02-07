package hardcoder.dev.healther.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import hardcoder.dev.healther.ui.screens.welcome.Gender
import kotlinx.coroutines.flow.map

class UserRepository(private val context: Context) {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
    private val PREFERENCE_WEIGHT_KEY = intPreferencesKey(WEIGHT_KEY)
    private val PREFERENCE_EXERCISE_STRESS_TIME_KEY = intPreferencesKey(EXERCISE_STRESS_KEY)
    private val PREFERENCE_GENDER_KEY = stringPreferencesKey(GENDER_KEY)

    val weight = context.userDataStore.data.map {
        it[PREFERENCE_WEIGHT_KEY] ?: 0
    }

    val exerciseStressTime = context.userDataStore.data.map {
        it[PREFERENCE_EXERCISE_STRESS_TIME_KEY] ?: 0
    }

    val gender = context.userDataStore.data.map {
        it[PREFERENCE_GENDER_KEY]?.let { genderValue -> Gender.valueOf(genderValue) } ?: Gender.MALE
    }

    suspend fun updateWeight(weight: Int) {
        context.userDataStore.edit { userData ->
            userData[PREFERENCE_WEIGHT_KEY] = weight
        }
    }

    suspend fun updateExerciseStressTime(exerciseStressTime: Int) {
        context.userDataStore.edit { userData ->
            userData[PREFERENCE_EXERCISE_STRESS_TIME_KEY] = exerciseStressTime
        }
    }

    suspend fun updateGender(gender: Gender) {
        context.userDataStore.edit { userData ->
            userData[PREFERENCE_GENDER_KEY] = gender.name
        }
    }

    companion object {
        private const val WEIGHT_KEY = "USER_WEIGHT_KEY"
        private const val EXERCISE_STRESS_KEY = "USER_EXERCISE_STRESS_TIME_KEY"
        private const val GENDER_KEY = "USER_GENDER_KEY"
    }
}