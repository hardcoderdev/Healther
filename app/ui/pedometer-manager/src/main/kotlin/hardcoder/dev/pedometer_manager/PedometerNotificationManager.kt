package hardcoder.dev.pedometer_manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import hardcoderdev.healther.app.ui.resources.R

class PedometerNotificationManager(private val context: Context) {

    private var currentNotification: Notification? = null
    private var notificationBuilder: Notification.Builder? = null
    private val contentIntentDeepLinkUri by lazy {
        context.getString(R.string.pedometer_notificationManager_contentIntent_deeplinkPattern)
            .toUri()
    }
    private val pedometerChannelId by lazy {
        context.getString(R.string.pedometer_notificationManagerChannelId)
    }
    private val pedometerChannelName by lazy {
        context.getString(R.string.pedometer_notificationManager_channelName)
    }

    fun getNotification(initialStepCount: Int): Notification? {
        val channelId = createNotificationChannel()

        notificationBuilder = Notification.Builder(context, channelId)
        val contentIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(Intent(Intent.ACTION_VIEW, contentIntentDeepLinkUri))
            getPendingIntent(
                CONTENT_INTENT_REQUEST_CODE,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        notificationBuilder?.apply {
            setContentTitle(context.getString(R.string.pedometer_notification_notificationContentTitle_text))
            setContentText(
                context.getString(
                    R.string.pedometer_notification_contentStepFormat_text,
                    initialStepCount,
                ),
            )
            setContentIntent(contentIntent)
            setSmallIcon(R.drawable.ic_pedometer_notification)
            setOngoing(true)
            setCategory(Notification.CATEGORY_STATUS)
            setVisibility(Notification.VISIBILITY_PUBLIC)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
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
                    R.string.pedometer_notification_contentStepFormat_text,
                    newStepCount,
                ),
            )
        }?.build()

        val service = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        service.notify(NOTIFICATION_ID, currentNotification)
    }

    private fun createNotificationChannel(): String {
        NotificationChannel(
            pedometerChannelId,
            pedometerChannelName,
            NotificationManager.IMPORTANCE_NONE,
        ).apply {
            lightColor = Color.GREEN
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            importance = NotificationManager.IMPORTANCE_DEFAULT
        }.also {
            val service =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(it)
        }

        return pedometerChannelId
    }

    companion object {
        private const val CONTENT_INTENT_REQUEST_CODE = 111
        const val NOTIFICATION_ID = 112
    }
}