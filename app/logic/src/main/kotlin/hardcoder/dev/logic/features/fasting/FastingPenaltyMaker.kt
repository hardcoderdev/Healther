package hardcoder.dev.logic.features.fasting

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.LastEntranceManager
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.fasting.track.FastingTrackProvider
import hardcoder.dev.logic.hero.health.HeroHealthPointsManager
import hardcoder.dev.logic.reward.penalty.PenaltyCalculator
import hardcoder.dev.logic.reward.penalty.PenaltyCreator
import hardcoder.dev.logic.reward.penalty.PunishableAction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class FastingPenaltyMaker(
    private val fastingTrackProvider: FastingTrackProvider,
    private val penaltyCalculator: PenaltyCalculator,
    private val penaltyCreator: PenaltyCreator,
    private val heroHealthPointsManager: HeroHealthPointsManager,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val lastEntranceManager: LastEntranceManager,
) {

    suspend fun givePenalty(dayRange: ClosedRange<Instant>) {
        val fastingTrackList = fastingTrackProvider.provideFastingTracksByStartTime(dayRange = dayRange).first()

        val dailyRateProgress = if (fastingTrackList.isNotEmpty()) 100 else 0
        val healthPointsToDecrease = penaltyCalculator.calculatePenaltyFor(
            punishableAction = PunishableAction.DailyRateProgress(
                currentProgressInPercentage = dailyRateProgress,
                inactiveDaysMultiplier = lastEntranceManager.calculateEntranceDifferenceInDays(),
            ),
        )

        withContext(dispatchers.io) {
            heroHealthPointsManager.decreaseHealth(healthPointsToDecrease = healthPointsToDecrease)
            penaltyCreator.create(
                featureType = FeatureType.FASTING,
                date = dateTimeProvider.currentInstant(),
                healthPointsAmount = healthPointsToDecrease,
            )
        }
    }
}