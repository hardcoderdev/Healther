package hardcoder.dev.logic.reward.experience

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Experience
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.reward.experience.Experience as ExperienceEntity

class ExperienceProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
) {

    fun provideExperiencesByDate(
        featureType: FeatureType,
        isCollected: Boolean,
        dayRange: ClosedRange<Instant>,
    ) = appDatabase.experienceQueries.provideAllExperiencesByDate(
        isCollected,
        featureTypeIdMapper.mapToId(featureType),
        dayRange.start,
        dayRange.endInclusive,
    ).asFlowOfList(coroutineDispatcher = dispatchers.io) { rewardDatabase ->
        rewardDatabase.toEntity()
    }

    fun isTrackCollected(
        featureType: FeatureType,
        linkedTrackId: Int,
    ) = appDatabase.experienceQueries.isTrackCollected(
        featureTypeId = featureTypeIdMapper.mapToId(featureType),
        linkedTrackId = linkedTrackId,
    ).asFlow().map {
        val isCollected = it.executeAsOneOrNull()
        isCollected ?: false
    }.flowOn(dispatchers.io)

    private fun Experience.toEntity() = ExperienceEntity(
        id = id,
        date = date,
        isCollected = isCollected,
        amount = experiencePointsAmount,
    )
}