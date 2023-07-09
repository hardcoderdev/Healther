package hardcoder.dev.inAppReview

class ReviewException(message: String?) : Exception(
    "Something went wrong during review. $message",
)