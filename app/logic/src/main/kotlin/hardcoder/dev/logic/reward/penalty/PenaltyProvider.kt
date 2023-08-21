package hardcoder.dev.logic.reward.penalty

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Penalty
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.datetime.Instant
import hardcoder.dev.logic.reward.penalty.Penalty as PenaltyEntity

class PenaltyProvider(
    private val appDatabase: AppDatabase,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllPenaltiesByDate(
        dateRange: ClosedRange<Instant>,
    ) = appDatabase.penaltyQueries.provideAllPenaltiesByDate(
        isCollected = false,
        date = dateRange.start,
        date_ = dateRange.endInclusive,
    ).asFlowOfList(coroutineDispatcher = dispatchers.io) { penaltyDatabase ->
        penaltyDatabase.toEntity()
    }

    private fun Penalty.toEntity() = PenaltyEntity(
        id = id,
        featureType = featureTypeIdMapper.mapToFeatureType(featureTypeId),
        date = date,
        isCollected = isCollected,
    )
}