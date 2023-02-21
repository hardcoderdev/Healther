package hardcoder.dev.logic.updaters

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.Hero
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HeroUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(hero: Hero) = withContext(dispatcher) {
        appDatabase.heroQueries.update(
            weight = hero.weight,
            exerciseStressTime = hero.exerciseStressTime,
            gender = hero.gender,
            id = hero.id
        )
    }
}