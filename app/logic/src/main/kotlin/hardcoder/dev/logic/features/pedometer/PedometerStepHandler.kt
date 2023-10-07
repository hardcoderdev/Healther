package hardcoder.dev.logic.features.pedometer

import hardcoder.dev.database.IdGenerator
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.reward.currency.CurrencyCalculator
import hardcoder.dev.logic.reward.currency.CurrencyCreator
import hardcoder.dev.logic.reward.currency.CurrencyType
import hardcoder.dev.logic.reward.currency.RewardableAction
import hardcoder.dev.logic.reward.experience.ExperienceAction
import hardcoder.dev.logic.reward.experience.ExperienceCalculator
import hardcoder.dev.logic.reward.experience.ExperienceCreator
import java.time.LocalDateTime
import kotlinx.coroutines.flow.first
import kotlinx.datetime.toKotlinLocalDateTime

class PedometerStepHandler(
    private val idGenerator: IdGenerator,
    private val pedometerTrackUpserter: PedometerTrackUpserter,
    private val dateTimeProvider: DateTimeProvider,
    private val pedometerDailyRateStepsProvider: PedometerDailyRateStepsProvider,
    private val currencyCreator: CurrencyCreator,
    private val currencyCalculator: CurrencyCalculator,
    private val experienceCreator: ExperienceCreator,
    private val experienceCalculator: ExperienceCalculator,
) {
    private var currentTrackId = idGenerator.nextId()
    private var currentTrackStepCount = 0
    private var currentTrackStartTime = LocalDateTime.now()

    suspend fun handleNewSteps(steps: Int) {
        if (LocalDateTime.now().hour > currentTrackStartTime.hour) {
            currentTrackId = idGenerator.nextId()
            currentTrackStartTime = LocalDateTime.now()
            currentTrackStepCount = 0
        }
        val currentTrackEndTime = LocalDateTime.now()

        currentTrackStepCount += steps

        pedometerTrackUpserter.upsert(
            id = currentTrackId,
            stepsCount = currentTrackStepCount,
            range = currentTrackStartTime.toKotlinLocalDateTime()..currentTrackEndTime.toKotlinLocalDateTime(),
        )

        val dailyRateProgress = steps.div(
            pedometerDailyRateStepsProvider.resolve().first().toFloat(),
        ).times(100)

        if (dailyRateProgress < 1.0f) {
            experienceCreator.create(
                date = dateTimeProvider.currentInstant(),
                isCollected = false,
                featureType = FeatureType.PEDOMETER,
                linkedTrackId = currentTrackId,
                experiencePointsAmount = experienceCalculator.calculateExperienceFor(
                    experienceAction = ExperienceAction.DailyRateProgress(
                        currentProgressInPercentage = dailyRateProgress,
                    ),
                ),
            )

            currencyCreator.create(
                date = dateTimeProvider.currentInstant(),
                currencyType = CurrencyType.COINS,
                isCollected = false,
                featureType = FeatureType.PEDOMETER,
                linkedTrackId = currentTrackId,
                currencyAmount = currencyCalculator.calculateRewardFor(
                    rewardableAction = RewardableAction.DailyRateProgress(
                        currentProgressInPercentage = dailyRateProgress,
                    ),
                ),
            )
        }
    }
}