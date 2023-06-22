package hardcoder.dev.logic.hero

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.withContext

class HeroCreator(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun createHero(
        weight: Int,
        exerciseStressTime: Int,
        gender: Gender
    ) = withContext(dispatchers.io) {
        appDatabase.heroQueries.insert(
            id = HERO_ID,
            weight = weight,
            exerciseStressTime = exerciseStressTime,
            genderId = genderIdMapper.mapToId(gender)
        )
    }
}