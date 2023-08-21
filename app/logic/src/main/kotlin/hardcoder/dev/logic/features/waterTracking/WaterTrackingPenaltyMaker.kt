package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.appPreferences.LastEntranceManager
import hardcoder.dev.logic.reward.penalty.PenaltyCalculator
import hardcoder.dev.logic.reward.penalty.PenaltyCreator
import hardcoder.dev.logic.reward.penalty.PunishableAction
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.hero.health.HeroHealthPointsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class WaterTrackingPenaltyMaker(
    private val millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider,
    private val penaltyCreator: PenaltyCreator,
    private val penaltyCalculator: PenaltyCalculator,
    private val heroHealthPointsManager: HeroHealthPointsManager,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val lastEntranceManager: LastEntranceManager,
) {

    suspend fun givePenalty(dateRange: ClosedRange<Instant>) {
        val millilitersDrunkToDailyRate = millilitersDrunkProvider.provideMillilitersDrunkToDailyRateToday(
            dateRange = dateRange,
        ).first()

        val dailyRateProgress = (millilitersDrunkToDailyRate.millilitersDrunkCount
            / millilitersDrunkToDailyRate.dailyWaterIntake) * 100
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