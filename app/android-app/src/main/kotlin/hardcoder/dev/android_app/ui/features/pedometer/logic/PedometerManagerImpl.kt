package hardcoder.dev.android_app.ui.features.pedometer.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import hardcoder.dev.android_app.ui.features.pedometer.PedometerService
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.entities.pedometer.PedometerTrack
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import hardcoder.dev.presentation.pedometer.PedometerManager
import hardcoder.dev.utilities.VersionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PedometerManagerImpl(
    private val context: Context,
    private val pedometerTrackProvider: PedometerTrackProvider,
    private val permissionsController: PermissionsController,
    private val batteryRequirements: BatteryRequirements
) : PedometerManager {

    private val managerScope = CoroutineScope(Dispatchers.IO + Job())
    private val isTrackingNow = MutableStateFlow(false)
    private val serviceIntent by lazy {
        Intent(context, PedometerService::class.java)
    }

    override fun isTrackingNow(): Flow<Boolean> {
        return isTrackingNow
    }

    override fun newStepsFlow(range: LongRange): Flow<List<PedometerTrack>> {
        return pedometerTrackProvider.providePedometerTracksByRange(range)
    }

    override fun startTracking() {
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

    override fun requestPermissions() {
        batteryRequirements.requestIgnoreBatteryOptimizations()
        managerScope.launch {
            if (VersionChecker.isTiramisu()) {
                permissionsController.requestPermissions(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            } else {
                permissionsController.requestPermissions(
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            }
        }
    }
}