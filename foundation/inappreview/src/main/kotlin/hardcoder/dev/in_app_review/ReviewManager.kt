package hardcoder.dev.in_app_review

interface ReviewManager {
    suspend fun launchReviewFlow(): Boolean
}