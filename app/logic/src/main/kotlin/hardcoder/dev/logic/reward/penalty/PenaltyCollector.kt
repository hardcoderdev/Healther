package hardcoder.dev.logic.reward.penalty

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import kotlinx.coroutines.withContext

class PenaltyCollector(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    suspend fun collect(featureType: FeatureType) = withContext(dispatchers.io) {
        appDatabase.penaltyQueries.collect(
            featureTypeId = featureTypeIdMapper.mapToId(featureType = featureType),
        )
    }
}