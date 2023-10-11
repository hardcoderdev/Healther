package hardcoder.dev.resolvers.features.waterTracking

import hardcoder.dev.entities.user.Gender
import kotlin.math.roundToInt

class WaterIntakeResolver {

    fun resolve(weight: Int, exerciseStressTime: Int, gender: Gender): Int {
        val stressTimeMultiplier = if (gender == Gender.MALE) {
            MALE_STRESS_TIME_MULTIPLIER
        } else {
            FEMALE_STRESS_TIME_MULTIPLIER
        }

        val dailyWaterIntakeInLiters =
            ((weight * WEIGHT_MULTIPLIER) + (exerciseStressTime * stressTimeMultiplier)).roundToInt()
        return dailyWaterIntakeInLiters * MILLILITERS_MULTIPLIER
    }

    private companion object {
        private const val MALE_STRESS_TIME_MULTIPLIER = 0.6
        private const val FEMALE_STRESS_TIME_MULTIPLIER = 0.6
        private const val WEIGHT_MULTIPLIER = 0.03
        private const val MILLILITERS_MULTIPLIER = 1000
    }
}