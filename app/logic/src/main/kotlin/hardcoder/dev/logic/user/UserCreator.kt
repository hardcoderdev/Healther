package hardcoder.dev.logic.user

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.user.gender.Gender
import hardcoder.dev.logic.user.gender.GenderIdMapper
import kotlinx.coroutines.withContext

class UserCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        gender: Gender,
        name: CorrectUserName,
        weight: CorrectUserWeight,
        exerciseStressTime: CorrectUserExerciseStressTime,
    ) = withContext(dispatchers.io) {
        appDatabase.userQueries.insert(
            id = idGenerator.nextId(),
            name = name.data,
            weight = weight.data.toInt(),
            exerciseStressTime = exerciseStressTime.data.toInt(),
            genderId = genderIdMapper.mapToId(gender),
        )
    }
}