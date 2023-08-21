package hardcoder.dev.logic.reward.penalty

class PenaltyCalculator {

    fun calculatePenaltyFor(punishableAction: PunishableAction) = when (punishableAction) {
        is PunishableAction.DailyRateProgress -> {
            val completedDailyRateHealthPoints = punishableAction.currentProgressInPercentage
                .div(100)
                .times(PENALTY_HEALTH_POINTS_DAILY_RATE)

            val penaltyForDailyRate = PENALTY_HEALTH_POINTS_DAILY_RATE
                .minus(completedDailyRateHealthPoints)
                .times(punishableAction.inactiveDaysMultiplier)

            penaltyForDailyRate
        }
    }
}

sealed class PunishableAction {
    data class DailyRateProgress(
        val currentProgressInPercentage: Int,
        val inactiveDaysMultiplier: Int,
    ) : PunishableAction()
}