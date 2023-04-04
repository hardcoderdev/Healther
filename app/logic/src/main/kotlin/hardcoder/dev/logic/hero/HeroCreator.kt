package hardcoder.dev.logic.hero

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HeroCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val genderIdMapper: GenderIdMapper
) {

    suspend fun createHero(
        weight: Int,
        exerciseStressTime: Int,
        gender: Gender
    ) = withContext(dispatcher) {
        appDatabase.heroQueries.insert(
            id = HERO_ID,
            weight = weight,
            exerciseStressTime = exerciseStressTime,
            genderId = genderIdMapper.mapToId(gender)
        )
    }
}