package hardcoder.dev.logic.reward.experience

class ExperienceCalculator {

    fun calculateExperienceFor(experienceAction: ExperienceAction) = when (experienceAction) {
        is ExperienceAction.DailyRateProgress -> {
            experienceAction.currentProgressInPercentage
                .div(100)
                .times(XP_FOR_FEATURE)
        }
    }
}

sealed class ExperienceAction {
    data class DailyRateProgress(
        val currentProgressInPercentage: Float,
    ) : ExperienceAction()
}