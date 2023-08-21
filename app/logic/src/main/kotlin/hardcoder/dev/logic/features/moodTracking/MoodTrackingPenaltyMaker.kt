package hardcoder.dev.logic.features.moodTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.LastEntranceManager
import hardcoder.dev.logic.reward.penalty.PenaltyCalculator
import hardcoder.dev.logic.reward.penalty.PenaltyCreator
import hardcoder.dev.logic.reward.penalty.PunishableAction
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDailyRateProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.hero.health.HeroHealthPointsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class MoodTrackingPenaltyMaker(
    private val moodTrackProvider: MoodTrackProvider,
    private val moodTrackDailyRateProvider: MoodTrackDailyRateProvider,
    private val penaltyCalculator: PenaltyCalculator,
    private val penaltyCreator: PenaltyCreator,
    private val heroHealthPointsManager: HeroHealthPointsManager,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val lastEntranceManager: LastEntranceManager,
) {

    suspend fun givePenalty(dayRange: ClosedRange<Instant>) {
        val moodTrackList = moodTrackProvider.provideAllMoodTracksByDayRange(dayRange = dayRange).first()
        val moodTrackingDailyRate = moodTrackDailyRateProvider.provide().first()

        val dailyRateProgress = (moodTrackList.count() / moodTrackingDailyRate) * 100
        val healthPointsToDecrease = penaltyCalculator.calculatePenaltyFor(
            punishableAction = PunishableAction.DailyRateProgress(
                currentProgressInPercentage = dailyRateProgress,
                inactiveDaysMultiplier = lastEntranceManager.calculateEntranceDifferenceInDays(),
            ),
        )

        withContext(dispatchers.io) {
            heroHealthPointsManager.decreaseHealth(healthPointsToDecrease = healthPointsToDecrease)
            penaltyCreator.create(
                featureType = FeatureType.DIARY,
                date = dateTimeProvider.currentInstant(),
                healthPointsAmount = healthPointsToDecrease,
            )
        }
    }
}