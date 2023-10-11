package hardcoder.dev.pedometer_manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BatteryRequirementsController {

    private val mutex = Mutex()
    private val lastResult = MutableStateFlow<Unit?>(null)
    private val isRequesting get() = mutex.isLocked

    private lateinit var powerManager: PowerManager
    private lateinit var packageName: String
    private var activity: ComponentActivity? = null

    private val activityObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            if (isRequesting) {
                lastResult.value = Unit
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            activity?.lifecycle?.removeObserver(this)
            activity = null
        }
    }

    fun bind(activity: ComponentActivity) {
        activity.lifecycle.addObserver(activityObserver)
        this.activity = activity
        powerManager = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
        packageName = activity.packageName
    }

    @SuppressLint("BatteryLife")
    suspend fun requestIgnoreBatteryOptimizations(): Boolean {
        val activity = checkNotNull(activity)
        return if (isIgnoringBatteryOptimizations()) {
            true
        } else {
            mutex.withLock {
                activity.startActivity(
                    Intent().apply {
                        action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        data = "package:$packageName".toUri()
                    },
                )

                lastResult.filterNotNull().first().also {
                    lastResult.value = null
                }

                delay(DIRTY_DELAY) // this data updating async
                isIgnoringBatteryOptimizations()
            }
        }
    }

    fun isIgnoringBatteryOptimizations() =
        powerManager.isIgnoringBatteryOptimizations(packageName)
}

private const val DIRTY_DELAY = 1000L