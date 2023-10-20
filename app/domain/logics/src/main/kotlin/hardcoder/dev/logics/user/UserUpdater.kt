package hardcoder.dev.logics.user

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.user.UserDao
import hardcoder.dev.mappers.user.GenderIdMapper
import kotlinx.coroutines.withContext
import hardcoder.dev.database.entities.user.User
import hardcoder.dev.entities.user.User as UserEntity

class UserUpdater(
    private val userDao: UserDao,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(user: UserEntity) = withContext(dispatchers.io) {
        userDao.update(
            User(
                weight = user.weight,
                exerciseStressTime = user.exerciseStressTime,
                genderId = genderIdMapper.mapToId(user.gender),
                id = USER_ID,
                name = user.name,
            )
        )
    }
}