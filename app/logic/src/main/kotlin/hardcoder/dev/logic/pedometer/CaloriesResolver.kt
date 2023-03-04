package hardcoder.dev.logic.pedometer

import hardcoder.dev.extensions.safeDiv

class CaloriesResolver {

    fun resolve(stepsCount: Int): Float = stepsCount safeDiv 1000 * CALORIES_ON_ONE_THOUSAND_STEPS

    companion object {
        private const val CALORIES_ON_ONE_THOUSAND_STEPS = 35
    }
}