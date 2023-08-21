package hardcoder.dev.logic.reward.experience

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class ExperienceCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    suspend fun create(
        featureType: FeatureType,
        linkedTrackId: Int,
        experiencePointsAmount: Float,
        date: Instant,
        isCollected: Boolean,
    ) = withContext(dispatchers.io) {
        appDatabase.experienceQueries.insert(
            id = idGenerator.nextId(),
            featureTypeId = featureTypeIdMapper.mapToId(featureType),
            linkedTrackId = linkedTrackId,
            experiencePointsAmount = experiencePointsAmount,
            date = date,
            isCollected = isCollected,
        )
    }
}