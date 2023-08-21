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
        gender: Gender,
        name: CorrectHeroName,
        weight: CorrectHeroWeight,
        exerciseStressTime: CorrectHeroHeroExerciseStress,
    ) = withContext(dispatchers.io) {
        appDatabase.heroQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            weight = weight.data.toInt(),
            exerciseStressTime = exerciseStressTime.data.toInt(),
            genderId = genderIdMapper.mapToId(gender),
            coins = HERO_INITIAL_COINS,
            crystals = HERO_INITIAL_CRYSTALS,
            currentHealthPoints = HERO_MAX_HEALTH_POINTS,
            maxHealthPoints = HERO_MAX_HEALTH_POINTS,
            experiencePoints = HERO_INITIAL_EXPERIENCE_POINTS,
            level = HERO_INITIAL_LEVEL,
        )
    }
}