package hardcoder.dev.logics.features.pedometer

import java.time.LocalDateTime
import kotlin.random.Random
import kotlinx.datetime.toKotlinLocalDateTime

class PedometerStepHandler(
    private val pedometerTrackUpserter: PedometerTrackUpserter,
) {
    private var currentTrackId = Random.nextInt()
    private var currentTrackStepCount = 0
    private var currentTrackStartTime = LocalDateTime.now()

    suspend fun handleNewSteps(steps: Int) {
        if (LocalDateTime.now().hour > currentTrackStartTime.hour) {
            currentTrackId = Random.nextInt()
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