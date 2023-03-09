package hardcoder.dev.androidApp.ui.features.pedometer.logic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.core.net.toUri

class BatteryRequirements(private val context: Context) {

    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    private val packageName: String = context.packageName

    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations() {
        if (powerManager.isIgnoringBatteryOptimizations(packageName).not()) {
            Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = "package:$packageName".toUri()
            }.also {
                context.startActivity(it)
            }
        }
    }
}