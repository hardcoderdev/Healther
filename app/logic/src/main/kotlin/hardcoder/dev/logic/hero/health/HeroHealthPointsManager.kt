package hardcoder.dev.logic.hero.health

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.hero.HeroProvider
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class HeroHealthPointsManager(
    private val appDatabase: AppDatabase,
    heroProvider: HeroProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {
    private val currentHeroHealthPoints = heroProvider.provideHero().filterNotNull().map {
        it.currentHealthPoints
    }

    suspend fun decreaseHealth(healthPointsToDecrease: Int) = withContext(dispatchers.io) {
        val previousHeathPoints = currentHeroHealthPoints.firstNotNull()
        val decreasedHealthPoints = previousHeathPoints - healthPointsToDecrease
        appDatabase.heroQueries.updateHealth(
            currentHealthPoints = decreasedHealthPoints,
        )
    }

    suspend fun increaseHealth() = withContext(dispatchers.io) {
        val previousHealthPoints = currentHeroHealthPoints.firstNotNull()
        val increasedHealthPoints = previousHealthPoints + 1
        appDatabase.heroQueries.updateHealth(
            currentHealthPoints = increasedHealthPoints,
        )
    }
}