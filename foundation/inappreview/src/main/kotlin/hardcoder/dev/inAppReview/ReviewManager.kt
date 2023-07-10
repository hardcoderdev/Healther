package hardcoder.dev.inAppReview

interface ReviewManager {
    suspend fun launchReviewFlow(): Boolean
}