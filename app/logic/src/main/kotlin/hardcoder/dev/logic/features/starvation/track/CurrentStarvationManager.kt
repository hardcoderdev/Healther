package hardcoder.dev.logic.features.starvation.track

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentStarvationManager(
    private val idGenerator: IdGenerator,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val appDatabase: AppDatabase,
    private val starvationPlanIdMapper: StarvationPlanIdMapper,
    private val starvationTrackProvider: StarvationTrackProvider
) {

    private val Context.starvationDataStore: DataStore<Preferences> by preferencesDataStore(name = STARVATION_DATA)
    private val STARVATION_CURRENT_TRACK_KEY = intPreferencesKey(STARVATION_CURRENT_TRACK_ID)
    private val starvationCurrentTrackId = context.starvationDataStore.data.map {
        it[STARVATION_CURRENT_TRACK_KEY] ?: 0
    }

    fun provideCurrentStarvationTrack() = starvationCurrentTrackId.flatMapLatest {
        starvationTrackProvider.provideStarvationTrackById(it)
    }

    suspend fun startStarvation(
        startTime: Long,
        duration: Long,
        starvationPlan: StarvationPlan
    ) = withContext(dispatcher) {
        val id = idGenerator.nextId()
        setCurrentId(id)

        appDatabase.starvationTrackQueries.insert(
            id = id,
            startTime = startTime,
            duration = duration,
            fullDuration = duration,
            starvationPlanId = starvationPlanIdMapper.mapToId(starvationPlan),
            interruptedTimeInMillis = null
        )
    }

    suspend fun interruptStarvation(duration: Long) = withContext(dispatcher) {
        appDatabase.starvationTrackQueries.update(
            id = starvationCurrentTrackId.first(),
            interruptedTimeInMillis = System.currentTimeMillis(),
            duration = duration
        )
    }

    suspend fun clearStarvation() = withContext(dispatcher) {
        setCurrentId(null)
    }

    private suspend fun setCurrentId(id: Int?) {
        context.starvationDataStore.edit { starvationData ->
            id?.let {
                starvationData[STARVATION_CURRENT_TRACK_KEY] = it
            } ?: run {
                starvationData.clear()
            }
        }
    }

    private companion object {
        private const val STARVATION_DATA = "HEALTHER_STARVATION_DATA"
        private const val STARVATION_CURRENT_TRACK_ID = "starvation_currentTrack_id"
    }
}