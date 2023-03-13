package hardcoder.dev.presentation.features.pedometer

import kotlinx.coroutines.flow.Flow

interface PedometerManager {
    fun isTrackingNow(): Flow<Boolean>
    suspend fun startTracking()
    fun stopTracking()
}