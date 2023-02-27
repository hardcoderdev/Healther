package hardcoder.dev.logic.pedometer

import hardcoder.dev.extensions.roundOffToThreeDecimal

class KilometersResolver {

    fun resolve(stepsCount: Int): Float {
        return (stepsCount.toFloat() / AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER).roundOffToThreeDecimal()
    }

    private companion object {
        private const val AVERAGE_STEPS_COUNT_IN_ONE_KILOMETER = 1429
    }
}