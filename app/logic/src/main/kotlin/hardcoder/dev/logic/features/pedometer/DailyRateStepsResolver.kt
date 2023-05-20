package hardcoder.dev.logic.features.pedometer

private const val DAILY_RATE_STEPS = 6000

class DailyRateStepsResolver {

    fun resolve(): Int {
        return DAILY_RATE_STEPS
    }
}