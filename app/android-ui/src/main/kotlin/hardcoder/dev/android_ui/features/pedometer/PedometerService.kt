package hardcoder.dev.android_ui.features.pedometer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import hardcoder.dev.android_ui.features.pedometer.PedometerNotificationManager.Companion.NOTIFICATION_ID
import hardcoder.dev.extensions.toast
import hardcoder.dev.healther.R

class PedometerService : Service(), SensorEventListener {

    private val pedometerNotificationManager by lazy { PedometerNotificationManager(this) }
    private val binder = PedometerBinder()
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val stepSensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) }
    var summaryStepCount = 0
    var previousStepCount = 0

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        if (stepSensor == null) {
            toast(msgResId = R.string.pedometerScreen_noHardwareSensorOnDevice_error)
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        val notification =
            pedometerNotificationManager.getNotification(initialStepCount = previousStepCount)
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        previousStepCount = summaryStepCount
        sensorManager.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        summaryStepCount += event?.values?.get(0)?.toInt() ?: 0
        pedometerNotificationManager.updateNotification(newStepCount = summaryStepCount)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    inner class PedometerBinder : Binder() {
        fun getService(): PedometerService = this@PedometerService
    }
}