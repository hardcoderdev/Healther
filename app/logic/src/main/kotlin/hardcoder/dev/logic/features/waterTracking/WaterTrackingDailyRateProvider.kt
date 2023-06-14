package hardcoder.dev.logic.features.waterTracking

import hardcoder.dev.logic.hero.HeroProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class WaterTrackingDailyRateProvider(
    private val waterIntakeResolver: WaterIntakeResolver,
    private val heroProvider: HeroProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideDailyRateInMilliliters(): Flow<Int> {
        return heroProvider.requireHero().map { hero ->
            waterIntakeResolver.resolve(
                hero.weight,
                hero.exerciseStressTime,
                hero.gender
            )
        }.flowOn(ioDispatcher)
    }
}