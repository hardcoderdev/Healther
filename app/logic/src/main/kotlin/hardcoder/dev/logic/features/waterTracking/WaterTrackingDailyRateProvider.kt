package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.logic.features.waterTracking.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class WaterTrackingDailyRateProvider(
    private val waterIntakeResolver: WaterIntakeResolver,
    private val heroProvider: HeroProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val genderIdMapper: GenderIdMapper,
) {

    fun provideDailyRateInMilliliters(): Flow<Int> {
        return heroProvider.provideHero().filterNotNull().distinctUntilChangedBy {
            it.weight or it.exerciseStressTime or genderIdMapper.mapToId(it.gender)
        }.map { hero ->
            waterIntakeResolver.resolve(
                hero.weight,
                hero.exerciseStressTime,
                hero.gender,
            )
        }.flowOn(dispatchers.io)
    }
}