package hardcoder.dev.logic.reward.currency

import android.util.Log
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class CurrencyCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val currencyTypeIdMapper: CurrencyTypeIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    suspend fun create(
        featureType: FeatureType,
        linkedTrackId: Int,
        currencyType: CurrencyType,
        currencyAmount: Float,
        date: Instant,
        isCollected: Boolean,
    ) = withContext(dispatchers.io) {
        Log.d("depdlped", "in creator $currencyAmount")
        appDatabase.currencyQueries.insert(
            id = idGenerator.nextId(),
            featureTypeId = featureTypeIdMapper.mapToId(featureType),
            linkedTrackId = linkedTrackId,
            currencyTypeId = currencyTypeIdMapper.mapToId(currencyType),
            currencyAmount = currencyAmount,
            date = date,
            isCollected = isCollected,
        )
    }
}