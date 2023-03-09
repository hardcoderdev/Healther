package hardcoder.dev.logic.pedometer

import hardcoder.dev.database.IdGenerator
import hardcoder.dev.extensions.createRangeForCurrentDay
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class PedometerServiceTracker(
    private val idGenerator: IdGenerator,
    private val pedometerTrackCreator: PedometerTrackCreator,
    private val pedometerTrackProvider: PedometerTrackProvider
) {
    private var lastTrackUpsertTime = 0L
    private var currentTrackId = idGenerator.nextId()
    private var currentTrackStepCount = 0
    private var currentTrackStartTime = LocalDateTime.now()

    suspend fun onNewData(steps: Int) {
        if (LocalDateTime.now().hour > currentTrackStartTime.hour) {
            currentTrackId = idGenerator.nextId()
            currentTrackStartTime = LocalDateTime.now()
            currentTrackStepCount = 0
        }
        val currentTrackEndTime = LocalDateTime.now()

        currentTrackStepCount += steps

        if (System.currentTimeMillis() - lastTrackUpsertTime < UPDATE_DELAY) return
        lastTrackUpsertTime = System.currentTimeMillis()

        pedometerTrackCreator.upsertPedometerTrack(
            id = currentTrackId,
            stepsCount = currentTrackStepCount,
            range = currentTrackStartTime.toKotlinLocalDateTime()..currentTrackEndTime.toKotlinLocalDateTime()
        )
    }

    fun provideLastSteps(): Flow<Int> {
        return pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay()
        ).map { pedometerTracks ->
            pedometerTracks.sumOf { it.stepsCount }
        }
    }

    companion object {
        private const val UPDATE_DELAY = 5_000
    }
}