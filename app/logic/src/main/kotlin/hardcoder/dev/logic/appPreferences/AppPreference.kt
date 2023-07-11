package hardcoder.dev.logic.appPreferences

import kotlinx.datetime.Instant

data class AppPreference(
    val firstLaunchTime: Instant,
    val lastAppReviewRequestTime: Instant?,
    val lastEntranceDateTime: Instant,
)