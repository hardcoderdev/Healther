package hardcoder.dev.logics.features.diary

import kotlinx.coroutines.flow.flowOf

private const val DIARY_DAILY_RATE = 3

class DiaryDailyRateProvider {

    fun provide() = flowOf(DIARY_DAILY_RATE)
}