package hardcoder.dev.androidApp.ui.features.pedometer.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerService
import hardcoder.dev.extensions.hasPermissions
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.NotAvailable
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.PedometerManager.Availability
import hardcoder.dev.presentation.features.pedometer.RejectReason
import hardcoder.dev.utilities.VersionChecker
import kotlinx.coroutines.flow.MutableStateFlow

class PedometerManagerImpl(
    private val context: Context,
    private val permissionsController: PermissionsController,
    private val batteryRequirementsController: BatteryRequirementsController
) : PedometerManager {

    private val permissions by lazy {
        if (VersionChecker.isTiramisu()) arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) else arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    }

    override val availability = MutableStateFlow(checkAvailability())
    override val isTracking = MutableStateFlow(false)
    private val serviceIntent by lazy {
        Intent(context, PedometerService::class.java)
    }

    override suspend fun startTracking() {
        if (VersionChecker.isOreo()) context.startForegroundService(serviceIntent)
        else context.startService(serviceIntent)

        isTracking.value = true
    }

    override suspend fun requestPermissions() {
        permissionsController.requestPermissions(permissions)
        availability.value = checkAvailability()
    }

    override suspend fun requestBattery() {
        batteryRequirementsController.requestIgnoreBatteryOptimizations()
        availability.value = checkAvailability()
    }

    override fun stopTracking() {
        context.stopService(serviceIntent)
        isTracking.value = false
    }

    private fun checkAvailability(): Availability {
        return when {
            !PedometerService.isAvailable(context) -> {
                NotAvailable(RejectReason.ServiceNotAvailable)
            }

            !batteryRequirementsController.isIgnoringBatteryOptimizations() -> {
                NotAvailable(RejectReason.BatteryNotIgnoreOptimizations)
            }

            !context.hasPermissions(permissions) -> {
                NotAvailable(RejectReason.PermissionsNotGranted)
            }

            else -> {
                Available
            }
        }
    }
}