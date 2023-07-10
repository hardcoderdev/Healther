package hardcoder.dev.inAppReview

import android.app.Activity

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private suspend fun ReviewManager.awaitReviewInfo() = suspendCoroutine { continuation ->
    requestReviewFlow().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result)
        } else {
            continuation.resumeWithException(ReviewException(task.exception?.message))
        }
    }
}

private suspend fun ReviewManager.launchReview(
    activity: Activity,
    reviewInfo: ReviewInfo,
) = suspendCoroutine { continuation ->
    launchReviewFlow(activity, reviewInfo).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(ReviewException(task.exception?.message))
        }
    }
}

suspend fun ReviewManager.requestReviewFlowAsync(
    activity: Activity,
) = launchReview(
    activity = activity,
    reviewInfo = awaitReviewInfo(),
)