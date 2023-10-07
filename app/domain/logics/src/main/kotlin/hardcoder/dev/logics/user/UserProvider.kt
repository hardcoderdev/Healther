package hardcoder.dev.logic.user

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.User
import hardcoder.dev.mappers.user.GenderIdMapper
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.user.User as UserEntity

class UserProvider(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideUser() = appDatabase.userQueries
        .provideCurrentUser()
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun User.toEntity() = UserEntity(
        id = id,
        weight = weight,
        exerciseStressTime = exerciseStressTime,
        gender = genderIdMapper.mapToGender(genderId),
        name = name,
    )
}