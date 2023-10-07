package hardcoder.dev.logic.features.moodTracking.moodTrack

import kotlinx.coroutines.flow.flowOf

private const val DAILY_RATE_POSITIVE_PERCENTAGE = 20

class MoodTrackDailyRateProvider {

    fun provide() = flowOf(DAILY_RATE_POSITIVE_PERCENTAGE)
}