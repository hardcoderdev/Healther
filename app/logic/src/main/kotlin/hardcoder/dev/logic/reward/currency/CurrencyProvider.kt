package hardcoder.dev.logic.reward.currency

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Currency
import hardcoder.dev.sqldelight.asFlowOfList
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.reward.currency.Currency as RewardEntity

class CurrencyProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val currencyTypeIdMapper: CurrencyTypeIdMapper,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    fun provideRewardsByDate(
        featureType: FeatureType,
        isCollected: Boolean,
        dayRange: ClosedRange<Instant>,
    ) = appDatabase.currencyQueries.provideAllCoinsByDate(
        isCollected = isCollected,
        featureTypeId = featureTypeIdMapper.mapToId(featureType),
        date = dayRange.start,
        date_ = dayRange.endInclusive,
        currencyTypeId = currencyTypeIdMapper.mapToId(CurrencyType.COINS),
    ).asFlowOfList(coroutineDispatcher = dispatchers.io) { rewardDatabase ->
        rewardDatabase.toEntity()
    }

    fun isTrackCollected(
        featureType: FeatureType,
        linkedTrackId: Int,
    ) = appDatabase.currencyQueries.isTrackCollected(
        featureTypeId = featureTypeIdMapper.mapToId(featureType),
        linkedTrackId = linkedTrackId,
    ).asFlow().map {
        val isCollected = it.executeAsOneOrNull()
        isCollected ?: false
    }.flowOn(dispatchers.io)

    private fun Currency.toEntity() = RewardEntity(
        id = id,
        date = date,
        isCollected = isCollected,
        currencyType = currencyTypeIdMapper.mapToCurrencyType(currencyTypeId),
        amount = currencyAmount,
    )
}