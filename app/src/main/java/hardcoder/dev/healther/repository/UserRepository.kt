package hardcoder.dev.healther.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
    private val PREFERENCE_WEIGHT_KEY = intPreferencesKey(WEIGHT_KEY)
    private val PREFERENCE_EXERCISE_STRESS_TIME_KEY = intPreferencesKey(EXERCISE_STRESS_KEY)
    private val PREFERENCE_GENDER_KEY = stringPreferencesKey(GENDER_KEY)
    private val PREFERENCE_FIRST_LAUNCH_KEY = booleanPreferencesKey(FIRST_LAUNCH_KEY)

    val weight = context.userDataStore.data.map {
        it[PREFERENCE_WEIGHT_KEY] ?: 0
    }

    val exerciseStressTime = context.userDataStore.data.map {
        it[PREFERENCE_EXERCISE_STRESS_TIME_KEY] ?: 0
    }

    val gender = context.userDataStore.data.map {
        it[PREFERENCE_GENDER_KEY]?.let { genderValue -> Gender.valueOf(genderValue) } ?: Gender.MALE
    }

    val isFirstLaunch = context.userDataStore.data.map {
        it[PREFERENCE_FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun updateWeight(weight: Int) {
        withContext(dispatcher) {
            context.userDataStore.edit { userData ->
                userData[PREFERENCE_WEIGHT_KEY] = weight
            }
        }
    }

    suspend fun updateExerciseStressTime(exerciseStressTime: Int) {
        withContext(dispatcher) {
            context.userDataStore.edit { userData ->
                userData[PREFERENCE_EXERCISE_STRESS_TIME_KEY] = exerciseStressTime
            }
        }
    }

    suspend fun updateGender(gender: Gender) {
        withContext(dispatcher) {
            context.userDataStore.edit { userData ->
                userData[PREFERENCE_GENDER_KEY] = gender.name
            }
        }
    }

    suspend fun updateFirstLaunch(isFirstLaunch: Boolean) {
        withContext(dispatcher) {
            context.userDataStore.edit { userData ->
                userData[PREFERENCE_FIRST_LAUNCH_KEY] = isFirstLaunch
            }
        }
    }

    companion object {
        private const val WEIGHT_KEY = "USER_WEIGHT_KEY"
        private const val EXERCISE_STRESS_KEY = "USER_EXERCISE_STRESS_TIME_KEY"
        private const val GENDER_KEY = "USER_GENDER_KEY"
        private const val FIRST_LAUNCH_KEY = "USER_FIRST_LAUNCH_KEY"
    }
}