package hardcoder.dev.logic.hero

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HeroUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val genderIdMapper: GenderIdMapper
) {

    suspend fun update(hero: Hero) = withContext(ioDispatcher) {
        appDatabase.heroQueries.update(
            weight = hero.weight,
            exerciseStressTime = hero.exerciseStressTime,
            genderId = genderIdMapper.mapToId(hero.gender),
            id = HERO_ID
        )
    }

    companion object {
        private const val HERO_ID = 0
    }
}