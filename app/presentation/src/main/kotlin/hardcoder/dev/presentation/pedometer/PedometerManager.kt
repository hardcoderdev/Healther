package hardcoder.dev.presentation.pedometer

import kotlinx.coroutines.flow.Flow

interface PedometerManager {
    fun isTrackingNow(): Flow<Boolean>
    suspend fun startTracking()
    fun stopTracking()
}