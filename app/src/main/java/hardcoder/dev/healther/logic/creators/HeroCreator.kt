package hardcoder.dev.healther.logic.creators

import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.entities.Gender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HeroCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun createHero(
        weight: Int,
        exerciseStressTime: Int,
        gender: Gender
    ) = withContext(dispatcher) {
        appDatabase.heroQueries.insert(
            id = null,
            weight = weight,
            exerciseStressTime = exerciseStressTime,
            gender = gender
        )
    }
}