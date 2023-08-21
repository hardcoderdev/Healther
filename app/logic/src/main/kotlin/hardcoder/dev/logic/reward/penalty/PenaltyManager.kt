package hardcoder.dev.logic.reward.penalty

import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.FeatureTypeProvider
import hardcoder.dev.logic.features.diary.DiaryPenaltyMaker
import hardcoder.dev.logic.features.fasting.FastingPenaltyMaker
import hardcoder.dev.logic.features.moodTracking.MoodTrackingPenaltyMaker
import hardcoder.dev.logic.features.pedometer.PedometerPenaltyMaker
import hardcoder.dev.logic.features.waterTracking.WaterTrackingPenaltyMaker
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

class PenaltyManager(
    dateTimeProvider: DateTimeProvider,
    private val waterTrackingPenaltyMaker: WaterTrackingPenaltyMaker,
    private val pedometerPenaltyMaker: PedometerPenaltyMaker,
    private val moodTrackingPenaltyMaker: MoodTrackingPenaltyMaker,
    private val diaryPenaltyMaker: DiaryPenaltyMaker,
    private val fastingPenaltyMaker: FastingPenaltyMaker,
    private val featureTypeProvider: FeatureTypeProvider,
    private val penaltyCollector: PenaltyCollector,
) {
    private val previousDay = dateTimeProvider.currentDate().minus(
        value = 1,
        unit = DateTimeUnit.DAY,
    )

    private val previousDayRange = dateTimeProvider.dateRange(
        startDate = previousDay,
        endDate = previousDay,
    )

    suspend fun collectPenalties() {
        waterTrackingPenaltyMaker.givePenalty(dateRange = previousDayRange)
        pedometerPenaltyMaker.givePenalty(previousDayRange)
        moodTrackingPenaltyMaker.givePenalty(previousDayRange)
        diaryPenaltyMaker.givePenalty(previousDayRange)
        fastingPenaltyMaker.givePenalty(previousDayRange)

        featureTypeProvider.provide().first().forEach {
            penaltyCollector.collect(featureType = it)
        }
    }
}