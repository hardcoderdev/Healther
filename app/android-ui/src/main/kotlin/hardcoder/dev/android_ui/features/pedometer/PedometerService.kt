package hardcoder.dev.android_ui.features.pedometer

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import hardcoder.dev.android_ui.App
import hardcoder.dev.android_ui.features.pedometer.PedometerNotificationManager.Companion.NOTIFICATION_ID
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.toast
import hardcoder.dev.healther.R
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class PedometerService : Service(), SensorEventListener {

    private var lastTrackUpsertTime = 0L
    private var currentTrackId = 0L
    private var currentTrackStepCount = 0
    private var currentTrackStartTime = LocalDateTime.now()
    private var currentTrackEndTime = currentTrackStartTime.withMinute(59)

    // TODO SHOW ON LOCK SCREEN

    private val logicModule by lazy { App.instance.presentationModule.logicModule }
    private val pedometerNotificationManager by lazy { PedometerNotificationManager(this) }
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepSensor == null) {
            toast(msgResId = R.string.pedometerScreen_noHardwareSensorOnDevice_error)
            return
        }

        currentTrackId = logicModule.idGenerator.nextId()
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        val notification = pedometerNotificationManager.getNotification(initialStepCount = 0)
        startForeground(NOTIFICATION_ID, notification)

        logicModule.pedometerTrackProvider.providePedometerTracksByRange(
            LocalDate.now().createRangeForCurrentDay()
        ).onEach { pedometerTracks ->
            val todayStepCount = pedometerTracks.sumOf { it.stepsCount }
            pedometerNotificationManager.updateNotification(newStepCount = todayStepCount)
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
        if (LocalDateTime.now().hour > currentTrackEndTime.hour) {
            currentTrackId = logicModule.idGenerator.nextId()
            currentTrackStartTime = LocalDateTime.now()
            currentTrackEndTime = currentTrackStartTime.withMinute(59)
            currentTrackStepCount = 0
        }

        currentTrackStepCount += event?.values?.get(0)?.toInt() ?: 0

        if (System.currentTimeMillis() - lastTrackUpsertTime < UPDATE_DELAY) return
        lastTrackUpsertTime = System.currentTimeMillis()

        serviceScope.launch {
            logicModule.pedometerTrackCreator.upsertPedometerTrack(
                id = currentTrackId,
                stepsCount = currentTrackStepCount,
                range = currentTrackStartTime.toKotlinLocalDateTime()..currentTrackEndTime.toKotlinLocalDateTime()
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    companion object {
        private const val UPDATE_DELAY = 5_000
    }
}