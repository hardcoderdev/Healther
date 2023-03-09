package hardcoder.dev.presentation.pedometer

import hardcoder.dev.entities.pedometer.PedometerTrack
import kotlinx.coroutines.flow.Flow

interface PedometerManager {
    fun isTrackingNow(): Flow<Boolean>
    fun newStepsFlow(range: LongRange): Flow<List<PedometerTrack>>
    fun startTracking()
    fun stopTracking()
    fun requestPermissions()
}