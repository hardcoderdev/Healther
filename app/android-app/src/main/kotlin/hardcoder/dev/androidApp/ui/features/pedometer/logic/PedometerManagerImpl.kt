package hardcoder.dev.androidApp.ui.features.pedometer.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerService
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.utilities.VersionChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PedometerManagerImpl(
    private val context: Context,
    private val permissionsController: PermissionsController,
    private val batteryRequirements: BatteryRequirements
) : PedometerManager {

    private val isTrackingNow = MutableStateFlow(false)
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

    override fun isTrackingNow(): Flow<Boolean> {
        return isTrackingNow
    }

    override suspend fun startTracking() {
        batteryRequirements.requestIgnoreBatteryOptimizations()
        permissionsController.requestPermissions(permissions)

        if (VersionChecker.isOreo()) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        isTrackingNow.value = true
    }

    override fun stopTracking() {
        context.stopService(serviceIntent)
        isTrackingNow.value = false
    }
}