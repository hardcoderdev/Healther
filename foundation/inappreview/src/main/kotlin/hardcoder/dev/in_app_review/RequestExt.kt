package hardcoder.dev.in_app_review

import android.app.Activity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReviewException(message: String?) : Exception(
    "Something went wrong during review. $message"
)

suspend fun ReviewManager.requestReviewFlowAsync(
    activity: Activity
) = suspendCoroutine { continuation ->
    requestReviewFlow().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo: ReviewInfo = task.result
            launchReviewFlow(activity, reviewInfo).addOnCompleteListener {
                continuation.resume(it.isSuccessful)
            }
        } else {
            continuation.resumeWithException(ReviewException(task.exception?.message))
        }
    }
}