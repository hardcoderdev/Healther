package hardcoder.dev.logic.reward.experience

import hardcoder.dev.math.safeDiv

class HeroExperiencePointsProgressResolver {

    fun resolve(currentExperiencePoints: Float, experiencePointsNeed: Float): Float {
        return currentExperiencePoints safeDiv  experiencePointsNeed
    }
}