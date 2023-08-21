package hardcoder.dev.logic.reward.currency

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import hardcoder.dev.logic.hero.HeroProvider
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CurrencyCollector(
    heroProvider: HeroProvider,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
    private val currencyTypeIdMapper: CurrencyTypeIdMapper,
    private val dateTimeProvider: DateTimeProvider,
) {
    private val currentHero = heroProvider.provideHero().filterNotNull()

    suspend fun collect(featureType: FeatureType) {
        val uncollectedCurrencySum = appDatabase.currencyQueries.provideAllCoinsByDate(
            isCollected = false,
            featureTypeId = featureTypeIdMapper.mapToId(featureType),
            date = dateTimeProvider.currentDateRange().start,
            date_ = dateTimeProvider.currentDateRange().endInclusive,
            currencyTypeId = currencyTypeIdMapper.mapToId(CurrencyType.COINS),
        )
            .asFlow()
            .flowOn(dispatchers.io)
            .map { getUncollectedCurrencyQuery ->
                getUncollectedCurrencyQuery.executeAsList().map {
                    it.currencyAmount
                }.sum()
            }
            .flowOn(dispatchers.default)

        withContext(dispatchers.io) {
            val previousCoins = currentHero.firstNotNull().coins
            val increasedCoins = previousCoins + uncollectedCurrencySum.first()

            appDatabase.heroQueries.updateCoins(coins = increasedCoins)
            appDatabase.currencyQueries.collect(
                featureTypeId = featureTypeIdMapper.mapToId(featureType = featureType),
            )
        }
    }
}