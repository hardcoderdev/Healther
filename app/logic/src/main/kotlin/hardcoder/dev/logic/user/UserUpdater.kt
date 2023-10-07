package hardcoder.dev.logic.user

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.user.gender.GenderIdMapper
import kotlinx.coroutines.withContext

class UserUpdater(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(user: User) = withContext(dispatchers.io) {
        appDatabase.userQueries.update(
            weight = user.weight,
            exerciseStressTime = user.exerciseStressTime,
            genderId = genderIdMapper.mapToId(user.gender),
            id = HERO_ID,
            name = user.name,
        )
    }
}