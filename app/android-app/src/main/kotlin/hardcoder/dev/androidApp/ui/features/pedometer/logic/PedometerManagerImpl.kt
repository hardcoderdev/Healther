package hardcoder.dev.androidApp.ui.features.pedometer.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerService
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.TrackingRejectReason
import hardcoder.dev.utilities.VersionChecker
import kotlinx.coroutines.flow.MutableStateFlow

class PedometerManagerImpl(
    private val context: Context,
    private val permissionsController: PermissionsController,
    private val batteryRequirementsController: BatteryRequirementsController
) : PedometerManager {

    override val isTracking = MutableStateFlow(false)
    private val serviceIntent by lazy {
        Intent(context, PedometerService::class.java)
    }
    private val permissions by lazy {
        if (VersionChecker.isTiramisu()) arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) else arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    }

    override suspend fun startTracking(): TrackingRejectReason? {
        if (PedometerService.isAvailable(context).not())
            return TrackingRejectReason.ServiceAvailability

        if (batteryRequirementsController.requestIgnoreBatteryOptimizations().not())
            return TrackingRejectReason.BatteryRequirements

        if (permissionsController.requestPermissions(permissions).any { !it.value })
            return TrackingRejectReason.Permissions

        if (VersionChecker.isOreo()) context.startForegroundService(serviceIntent)
        else context.startService(serviceIntent)

        isTracking.value = true
        return null
    }

    override fun stopTracking() {
        context.stopService(serviceIntent)
        isTracking.value = false
    }
}