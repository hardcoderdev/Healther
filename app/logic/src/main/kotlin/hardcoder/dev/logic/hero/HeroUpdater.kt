package hardcoder.dev.logic.hero

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.withContext

class HeroUpdater(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(hero: Hero) = withContext(dispatchers.io) {
        appDatabase.heroQueries.update(
            weight = hero.weight,
            exerciseStressTime = hero.exerciseStressTime,
            genderId = genderIdMapper.mapToId(hero.gender),
            id = HERO_ID,
            name = hero.name,
            healthPoints = hero.healthPoints,
            experiencePoints = hero.experiencePoints,
        )
    }
}