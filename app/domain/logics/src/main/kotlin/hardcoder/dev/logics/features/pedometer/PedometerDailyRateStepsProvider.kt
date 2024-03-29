package hardcoder.dev.logics.features.pedometer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private const val DAILY_RATE_STEPS = 6000

class PedometerDailyRateStepsProvider {

    fun resolve(): Flow<Int> {
        return flowOf(DAILY_RATE_STEPS)
    }
}