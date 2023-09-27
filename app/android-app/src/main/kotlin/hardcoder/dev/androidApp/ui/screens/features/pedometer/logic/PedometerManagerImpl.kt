package hardcoder.dev.androidApp.ui.screens.features.pedometer.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import hardcoder.dev.androidApp.ui.screens.features.pedometer.PedometerService
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.presentation.features.pedometer.PedometerManager.Availability
import hardcoder.dev.presentation.features.pedometer.RejectReason
import kotlinx.coroutines.flow.MutableStateFlow

class PedometerManagerImpl(
    private val context: Context,
    private val permissionsController: PermissionsController,
    private val batteryRequirementsController: BatteryRequirementsController,
) : PedometerManager {

    private val permissions by lazy {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                arrayOf(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                )
            }
            else -> {
                emptyArray()
            }
        }
    }

    override val availability = MutableStateFlow(checkAvailability())
    override val isTracking = MutableStateFlow(false)
    private val serviceIntent by lazy {
        Intent(context, PedometerService::class.java)
    }

    override suspend fun startTracking() {
        context.startForegroundService(serviceIntent)
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
                Availability.NotAvailable(RejectReason.ServiceNotAvailable)
            }

            !batteryRequirementsController.isIgnoringBatteryOptimizations() -> {
                Availability.NotAvailable(RejectReason.BatteryNotIgnoreOptimizations)
            }

            permissions.any {
                ActivityCompat.checkSelfPermission(
                    context,
                    it,
                ) != PackageManager.PERMISSION_GRANTED
            } -> {
                Availability.NotAvailable(RejectReason.PermissionsNotGranted)
            }

            else -> {
                Availability.Available
            }
        }
    }
}