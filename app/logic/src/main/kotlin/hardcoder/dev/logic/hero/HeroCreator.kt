package hardcoder.dev.logic.hero

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.withContext

class HeroCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: String,
        weight: Int,
        exerciseStressTime: Int,
        gender: Gender,
    ): Int {
        val userId = idGenerator.nextId()

        withContext(dispatchers.io) {
            appDatabase.heroQueries.insert(
                id = userId,
                name = name,
                weight = weight,
                exerciseStressTime = exerciseStressTime,
                genderId = genderIdMapper.mapToId(gender),
                healthPoints = HERO_INITIAL_HEALTH_POINTS,
                experiencePoints = HERO_INITIAL_EXPERIENCE_POINTS,
            )
        }

        return userId
    }
}