package hardcoder.dev.presentation.features.pedometer

import kotlinx.coroutines.flow.StateFlow

interface PedometerManager {
    val isTracking: StateFlow<Boolean>
    val availability: StateFlow<Availability>

    suspend fun startTracking()
    suspend fun requestPermissions()
    suspend fun requestBattery()
    fun stopTracking()

    sealed class Availability
}

object Available : PedometerManager.Availability()

data class NotAvailable(
    val rejectReason: RejectReason
) : PedometerManager.Availability()

sealed class RejectReason {
    object BatteryNotIgnoreOptimizations : RejectReason()
    object PermissionsNotGranted : RejectReason()
    object ServiceNotAvailable : RejectReason()
}
