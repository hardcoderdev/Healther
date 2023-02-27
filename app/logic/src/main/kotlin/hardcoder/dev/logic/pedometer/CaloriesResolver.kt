package hardcoder.dev.logic.pedometer

import hardcoder.dev.extensions.roundOffToThreeDecimal


class CaloriesResolver {

    fun resolve(stepsCount: Int): Float {
        return (stepsCount.toFloat() / 1000 * CALORIES_ON_ONE_THOUSAND_STEPS).roundOffToThreeDecimal()
    }

    companion object {
        private const val CALORIES_ON_ONE_THOUSAND_STEPS = 35
    }
}