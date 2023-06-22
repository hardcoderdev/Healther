package hardcoder.dev.logic.features.fasting.track

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.dataStore.healtherDataStore
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.logic.features.fasting.plan.FastingPlanIdMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentFastingManager(
    private val idGenerator: IdGenerator,
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val fastingPlanIdMapper: FastingPlanIdMapper,
    private val fastingTrackProvider: FastingTrackProvider,
    private val diaryTrackCreator: DiaryTrackCreator,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    private val fastingCurrentTrackIdPreferenceKey = intPreferencesKey(FASTING_CURRENT_TRACK_ID)
    private val fastingCurrentTrackId = context.healtherDataStore.data.map {
        it[fastingCurrentTrackIdPreferenceKey] ?: 0
    }

    fun provideCurrentFastingTrack() = fastingCurrentTrackId.flatMapLatest {
        fastingTrackProvider.provideFastingTrackById(it)
    }

    suspend fun startFasting(
        startTime: Instant,
        duration: Long,
        fastingPlan: FastingPlan
    ) = withContext(dispatchers.io) {
        val id = idGenerator.nextId()
        setCurrentId(id)

        appDatabase.fastingTrackQueries.insert(
            id = id,
            startTime = startTime,
            duration = duration,
            fastingPlanId = fastingPlanIdMapper.mapToId(fastingPlan),
            interruptedTime = null
        )
    }

    suspend fun interruptFasting() = withContext(dispatchers.io) {
        appDatabase.fastingTrackQueries.update(
            id = fastingCurrentTrackId.first(),
            interruptedTime = Clock.System.now()
        )
    }

    suspend fun clearFasting(note: String) {
        if (note.isNotBlank()) {
            diaryTrackCreator.create(
                content = note,
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                diaryAttachmentGroup = DiaryAttachmentGroup(
                    fastingTracks = listOf(
                        fastingTrackProvider.provideFastingTrackById(fastingCurrentTrackId.first())
                            .filterNotNull().first()
                    )
                )
            )
        }
        setCurrentId(null)
    }

    private suspend fun setCurrentId(id: Int?) = withContext(dispatchers.io) {
        context.healtherDataStore.edit { fastingData ->
            id?.let {
                fastingData[fastingCurrentTrackIdPreferenceKey] = it
            } ?: run {
                fastingData.remove(fastingCurrentTrackIdPreferenceKey)
            }
        }
    }

    private companion object {
        private const val FASTING_CURRENT_TRACK_ID = "fasting_currentTrack_id"
    }
}