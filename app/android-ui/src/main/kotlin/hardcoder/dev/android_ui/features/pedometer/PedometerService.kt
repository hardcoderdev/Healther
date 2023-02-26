package hardcoder.dev.android_ui.features.pedometer

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.viewModelScope
import hardcoder.dev.android_ui.features.pedometer.PedometerNotificationManager.Companion.NOTIFICATION_ID
import hardcoder.dev.di.LogicModule
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.toast
import hardcoder.dev.healther.R
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class PedometerService : Service(), SensorEventListener {

    private var lastUpsertTime = 0L
    private var currentTrackId: Long = 0L
    private var startTime = LocalDateTime.now()
    private val logicModule by lazy { LogicModule(this) }
    private val pedometerTrackCreator by lazy { logicModule.pedometerTrackCreator }
    private val pedometerTrackProvider by lazy { logicModule.pedometerTrackProvider }
    private val idGenerator by lazy { logicModule.idGenerator }
    private val pedometerNotificationManager by lazy { PedometerNotificationManager(this) }
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val stepSensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) }
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var summaryStepCount = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay()
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.stateIn(
        scope = serviceScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    override fun onCreate() {
        super.onCreate()

        if (stepSensor == null) {
            toast(msgResId = R.string.pedometerScreen_noHardwareSensorOnDevice_error)
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        val notification =
            pedometerNotificationManager.getNotification(initialStepCount = summaryStepCount.value)
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?) = null

    override fun onSensorChanged(event: SensorEvent?) {
        var summary = summaryStepCount.value
        summary += event?.values?.get(0)?.toInt() ?: 0

        var endTime = LocalDateTime.now().withMinute(59)

        if (endTime.hour > startTime.hour) {
            currentTrackId = idGenerator.nextId()

            startTime = LocalDateTime.now()
             endTime = LocalDateTime.now().withMinute(59)
        }

        if (System.currentTimeMillis() - lastUpsertTime > 5000) {
            serviceScope.launch {
                pedometerTrackCreator.createPedometerTrack(
                    id = currentTrackId,
                    stepsCount = summary,
                    range = startTime.toKotlinLocalDateTime()..endTime.toKotlinLocalDateTime()
                )
                lastUpsertTime = System.currentTimeMillis()
            }
        }

        pedometerNotificationManager.updateNotification(newStepCount = summaryStepCount.value)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}