package hardcoder.dev.presentation.features.pedometer

import kotlinx.coroutines.flow.StateFlow

interface PedometerManager {
    val isTracking: StateFlow<Boolean>
    suspend fun startTracking(): TrackingRejectReason?
    fun stopTracking()
}

enum class TrackingRejectReason {
    BatteryRequirements,
    Permissions,
    ServiceAvailability
}