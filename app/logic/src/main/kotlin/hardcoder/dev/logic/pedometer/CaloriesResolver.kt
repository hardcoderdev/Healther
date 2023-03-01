package hardcoder.dev.logic.pedometer

class CaloriesResolver {

    fun resolve(stepsCount: Int): Float {
        return (stepsCount.toFloat() / 1000 * CALORIES_ON_ONE_THOUSAND_STEPS)
    }

    companion object {
        private const val CALORIES_ON_ONE_THOUSAND_STEPS = 35
    }
}