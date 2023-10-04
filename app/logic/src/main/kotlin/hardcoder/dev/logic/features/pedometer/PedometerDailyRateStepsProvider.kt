package hardcoder.dev.logic.features.pedometer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private const val DAILY_RATE_STEPS = 30

class PedometerDailyRateStepsProvider {

    fun resolve(): Flow<Int> {
        return flowOf(DAILY_RATE_STEPS)
    }
}