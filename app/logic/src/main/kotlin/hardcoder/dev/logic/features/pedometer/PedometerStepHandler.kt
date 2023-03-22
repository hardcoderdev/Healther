package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.database.IdGenerator
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class PedometerStepHandler(
    private val idGenerator: IdGenerator,
    private val pedometerTrackCreator: PedometerTrackCreator
) {
    private var lastTrackUpsertTime = 0L
    private var currentTrackId = idGenerator.nextId()
    private var currentTrackStepCount = 0
    private var currentTrackStartTime = LocalDateTime.now()

    suspend fun handleNewSteps(steps: Int) {
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

    companion object {
        private const val UPDATE_DELAY = 5_000
    }
}