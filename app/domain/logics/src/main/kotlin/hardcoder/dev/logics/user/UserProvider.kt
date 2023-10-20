package hardcoder.dev.logics.user

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.user.UserDao
import hardcoder.dev.database.entities.user.User
import hardcoder.dev.entities.user.Gender
import hardcoder.dev.mappers.user.GenderIdMapper
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.user.User as UserEntity

class UserProvider(
    private val userDao: UserDao,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideUser() = userDao
        .provideCurrentUser()
        .map { it.toEntity(gender = genderIdMapper.mapToGender(it.genderId)) }
        .flowOn(dispatchers.io)
}

private fun User.toEntity(gender: Gender) = UserEntity(
    id = id,
    weight = weight,
    exerciseStressTime = exerciseStressTime,
    gender = gender,
    name = name,
)