package hardcoder.dev.logic.hero.health

import hardcoder.dev.logic.hero.HERO_MAX_HEALTH_POINTS
import hardcoder.dev.math.safeDiv

class HeroHealthPointsResolver {

    fun resolve(currentHealthPoints: Int): Float {
        return currentHealthPoints safeDiv HERO_MAX_HEALTH_POINTS
    }
}