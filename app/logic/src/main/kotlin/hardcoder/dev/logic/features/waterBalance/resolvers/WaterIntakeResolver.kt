package hardcoder.dev.logic.features.waterBalance.resolvers

import hardcoder.dev.entities.hero.Gender
import kotlin.math.roundToInt

class WaterIntakeResolver {

    fun resolve(weight: Int, stressTime: Int, gender: Gender): Int {
        val stressTimeMultiplier = if (gender == Gender.MALE) {
            MALE_STRESS_TIME_MULTIPLIER
        } else {
            FEMALE_STRESS_TIME_MULTIPLIER
        }

        val dailyWaterIntakeInLiters =
            ((weight * WEIGHT_MULTIPLIER) + (stressTime * stressTimeMultiplier)).roundToInt()
        return dailyWaterIntakeInLiters * MILLILITERS_MULTIPLIER
    }

    private companion object {
        private const val MALE_STRESS_TIME_MULTIPLIER = 0.6
        private const val FEMALE_STRESS_TIME_MULTIPLIER = 0.6
        private const val WEIGHT_MULTIPLIER = 0.03
        private const val MILLILITERS_MULTIPLIER = 1000
    }
}