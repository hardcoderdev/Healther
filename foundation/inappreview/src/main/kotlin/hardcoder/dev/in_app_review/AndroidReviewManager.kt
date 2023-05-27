package hardcoder.dev.in_app_review

import android.app.Activity
import java.lang.ref.WeakReference
import com.google.android.play.core.review.ReviewManager as GoogleReviewManager

class AndroidReviewManager(private val reviewManager: GoogleReviewManager) : ReviewManager {

    private lateinit var activity: WeakReference<Activity>

    fun bind(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    override suspend fun launchReviewFlow() = runCatching {
        reviewManager.requestReviewFlowAsync(activity.get()!!)
    }.isSuccess
}