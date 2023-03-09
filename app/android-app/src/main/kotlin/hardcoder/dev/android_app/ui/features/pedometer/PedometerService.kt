package hardcoder.dev.android_app.ui.features.pedometer

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import hardcoder.dev.android_app.ui.App
import hardcoder.dev.android_app.ui.features.pedometer.PedometerNotificationManager.Companion.NOTIFICATION_ID
import hardcoder.dev.extensions.toast
import hardcoder.dev.healther.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PedometerService : Service(), SensorEventListener {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val pedometerServiceTracker by lazy { logicModule.pedometerServiceTracker }
    private val logicModule by lazy { App.instance.presentationModule.logicModule }
    private val pedometerNotificationManager by lazy { PedometerNotificationManager(this) }
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    override fun onCreate() {
        super.onCreate()
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepSensor == null) {
            toast(msgResId = R.string.pedometer_noHardwareSensorOnDevice_error)
            return
        }

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        val notification = pedometerNotificationManager.getNotification(initialStepCount = 0)
        startForeground(NOTIFICATION_ID, notification)

        pedometerServiceTracker.provideLastSteps().onEach {
            pedometerNotificationManager.updateNotification(newStepCount = it)
        }.launchIn(serviceScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        sensorManager.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?) = null

    override fun onSensorChanged(event: SensorEvent?) {
        serviceScope.launch {
            pedometerServiceTracker.onNewData(event?.values?.get(0)?.toInt() ?: 0)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}