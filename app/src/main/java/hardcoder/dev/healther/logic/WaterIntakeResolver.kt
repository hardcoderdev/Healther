package hardcoder.dev.healther.logic

import hardcoder.dev.healther.ui.screens.setUpFlow.gender.Gender
import kotlin.math.roundToInt

class WaterIntakeResolver {

    fun resolve(weight: Int, stressTime: Int, gender: Gender): Int {
        val stressTimeMultiplier =
            if (gender == Gender.MALE) 0.6 else 0.4
        val dailyWaterIntakeInLiters =
            ((weight * 0.03) + (stressTime * stressTimeMultiplier)).roundToInt()
        return dailyWaterIntakeInLiters * 1000
    }
}