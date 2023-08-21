package hardcoder.dev.logic.reward.currency

import android.util.Log

class CurrencyCalculator {

    fun calculateRewardFor(rewardableAction: RewardableAction) = when (rewardableAction) {
        is RewardableAction.DailyRateProgress -> {
            Log.d(
                "depdlped",
                "${
                    rewardableAction.currentProgressInPercentage
                        .div(100)
                        .times(REWARD_DAILY_RATE_PROGRESS)
                }",
            )
            rewardableAction.currentProgressInPercentage
                .div(100)
                .times(REWARD_DAILY_RATE_PROGRESS)
        }
    }
}

sealed class RewardableAction {
    data class DailyRateProgress(
        val currentProgressInPercentage: Float,
    ) : RewardableAction()
}