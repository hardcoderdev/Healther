package hardcoder.dev.presentation.features.pedometer

import kotlinx.coroutines.flow.StateFlow

interface PedometerManager {
    val isTracking: StateFlow<Boolean>
    val availability: StateFlow<Availability>

    suspend fun startTracking()
    suspend fun requestPermissions()
    suspend fun requestBattery()
    fun stopTracking()

    sealed class Availability {
        data object Available : Availability()

        data class NotAvailable(
            val rejectReason: RejectReason,
        ) : Availability()
    }
}

suspend inline fun PedometerManager.toggleTracking() {
    if (isTracking.value) {
        stopTracking()
    } else {
        startTracking()
    }
}

sealed class RejectReason {
    data object BatteryNotIgnoreOptimizations : RejectReason()
    data object PermissionsNotGranted : RejectReason()
    data object ServiceNotAvailable : RejectReason()
}