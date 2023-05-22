package hardcoder.dev.androidApp.ui.settings

import android.app.Activity
import hardcoder.dev.in_app_review.requestReviewFlowAsync
import hardcoder.dev.logic.settings.ReviewManager
import com.google.android.play.core.review.ReviewManager as GoogleReviewManager
import java.lang.ref.WeakReference

class AndroidReviewManager(private val reviewManager: GoogleReviewManager) : ReviewManager {

    private lateinit var activity: WeakReference<Activity>

    fun bind(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    override suspend fun launchReviewFlow(): Boolean {
        return activity.get()?.let { reviewManager.requestReviewFlowAsync(it) } ?: false
    }
}