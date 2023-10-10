package hardcoder.dev.logics.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.logics.user.UserProvider
import hardcoder.dev.mappers.user.GenderIdMapper
import hardcoder.dev.resolvers.features.waterTracking.WaterIntakeResolver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class WaterTrackingDailyRateProvider(
    private val waterIntakeResolver: WaterIntakeResolver,
    private val userProvider: UserProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val genderIdMapper: GenderIdMapper,
) {

    fun provideDailyRateInMilliliters(): Flow<Int> {
        return userProvider.provideUser().filterNotNull().distinctUntilChangedBy {
            it.weight or it.exerciseStressTime or genderIdMapper.mapToId(it.gender)
        }.map { user ->
            waterIntakeResolver.resolve(
                weight = user.weight,
                exerciseStressTime = user.exerciseStressTime,
                gender = user.gender,
            )
        }.flowOn(dispatchers.io)
    }
}