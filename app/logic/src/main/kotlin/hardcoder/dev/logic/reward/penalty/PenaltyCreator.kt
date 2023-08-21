package hardcoder.dev.logic.reward.penalty

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class PenaltyCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    suspend fun create(
        featureType: FeatureType,
        date: Instant,
        healthPointsAmount: Int,
    ) = withContext(dispatchers.io) {
        appDatabase.penaltyQueries.insert(
            id = idGenerator.nextId(),
            healthPointsAmount = healthPointsAmount,
            featureTypeId = featureTypeIdMapper.mapToId(featureType),
            date = date,
            isCollected = false,
        )
    }
}