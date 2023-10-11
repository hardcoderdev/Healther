package hardcoder.dev.pedometer_manager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import hardcoder.dev.logics.features.pedometer.PedometerStepHandler
import hardcoder.dev.pedometer_manager.PedometerNotificationManager.Companion.NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PedometerService : Service(), SensorEventListener {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val pedometerStepHandler by inject<PedometerStepHandler>()
    private val pedometerNotificationManager by lazy { PedometerNotificationManager(this) }
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    override fun onCreate() {
        super.onCreate()
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        checkNotNull(stepSensor)

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        val notification = pedometerNotificationManager.getNotification(initialStepCount = 0)
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }

    override fun onBind(intent: Intent?) = null

    override fun onSensorChanged(event: SensorEvent?) {
        serviceScope.launch {
            pedometerStepHandler.handleNewSteps(event?.values?.get(0)?.toInt() ?: 0)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        /* no-op */
    }

    private fun stopTracking() {
        serviceScope.cancel()

        sensorManager.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        fun isAvailable(context: Context): Boolean {
            val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
            val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            return stepSensor != null
        }
    }
}