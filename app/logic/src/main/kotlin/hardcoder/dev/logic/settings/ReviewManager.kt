package hardcoder.dev.logic.settings

interface ReviewManager {
    suspend fun launchReviewFlow(): Boolean
}