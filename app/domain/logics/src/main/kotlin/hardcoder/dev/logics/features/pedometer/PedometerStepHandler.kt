package hardcoder.dev.logics.features.pedometer

import hardcoder.dev.identification.IdGenerator
import java.time.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

class PedometerStepHandler(
    private val idGenerator: IdGenerator,
    private val pedometerTrackUpserter: PedometerTrackUpserter,
) {
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

        pedometerTrackUpserter.upsert(
            id = currentTrackId,
            stepsCount = currentTrackStepCount,
            range = currentTrackStartTime.toKotlinLocalDateTime()..currentTrackEndTime.toKotlinLocalDateTime(),
        )
    }
}