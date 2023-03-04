package hardcoder.dev.logic.pedometer

import hardcoder.dev.extensions.safeDiv

class KilometersResolver {

    fun resolve(stepsCount: Int): Float = stepsCount safeDiv AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER

    private companion object {
        private const val AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER = 1429
    }
}