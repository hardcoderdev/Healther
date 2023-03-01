package hardcoder.dev.android_ui.features.pedometer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import hardcoder.dev.android_ui.MainActivity
import hardcoder.dev.healther.R
import hardcoder.dev.utilities.VersionChecker

class PedometerNotificationManager(private val context: Context) {

    private var currentNotification: Notification? = null
    private var notificationBuilder: Notification.Builder? = null

    fun getNotification(initialStepCount: Int): Notification? {
        val channelId = createNotificationChannel()

        notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
        } else {
            Notification.Builder(context)
        }

        // TODO OPEN PEDOMETER WITH PENDING CONTENT INTENT
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            // putExtra("NOTIFICATION_DESTINATION", R.id.recordVoiceFragment)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationBuilder?.apply {
            setContentTitle(context.getString(R.string.pedometerNotification_notificationContentTitle_text))
            setContentText(
                context.getString(
                    R.string.pedometerNotification_contentStepFormat_text,
                    initialStepCount
                )
            )
            setContentIntent(contentPendingIntent)
            setSmallIcon(R.drawable.ic_pedometer_notification)
            setOngoing(true)
            setCategory(Notification.CATEGORY_STATUS)
            setVisibility(Notification.VISIBILITY_PUBLIC)
            if (VersionChecker.isNougatOrLower()) {
                setPriority(Notification.PRIORITY_HIGH)
            }
        }

        currentNotification = notificationBuilder?.build()
        return currentNotification
    }

    fun updateNotification(newStepCount: Int) {
        notificationBuilder?.apply {
            setContentText(
                context.getString(
                    R.string.pedometerNotification_contentStepFormat_text,
                    newStepCount
                )
            )
        }?.build()

        val service = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        service.notify(NOTIFICATION_ID, currentNotification)
    }

    private fun createNotificationChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                PEDOMETER_CHANNEL_ID,
                PEDOMETER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            ).apply {
                lightColor = Color.GREEN
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                importance = NotificationManager.IMPORTANCE_DEFAULT
            }.also {
                val service =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                service.createNotificationChannel(it)
            }
            PEDOMETER_CHANNEL_ID
        } else {
            ""
        }
    }

    companion object {
        const val NOTIFICATION_ID = 212
        private const val PEDOMETER_CHANNEL_ID = "healther_pedometer"
        private const val PEDOMETER_CHANNEL_NAME = "healther_pedometer"
    }
}