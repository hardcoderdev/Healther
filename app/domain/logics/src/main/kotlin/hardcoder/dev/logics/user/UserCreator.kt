package hardcoder.dev.logics.user

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.user.Gender
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.mappers.user.GenderIdMapper
import hardcoder.dev.validators.user.CorrectUserExerciseStressTime
import hardcoder.dev.validators.user.CorrectUserName
import hardcoder.dev.validators.user.CorrectUserWeight
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