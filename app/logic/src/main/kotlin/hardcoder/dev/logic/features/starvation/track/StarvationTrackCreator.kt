package hardcoder.dev.logic.features.starvation.track

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.logic.features.starvation.plan.StarvationPlanIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class StarvationTrackCreator(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val starvationPlanIdMapper: StarvationPlanIdMapper
) {

    suspend fun create(
        id: Int,
        startTime: Long,
        duration: Long,
        starvationPlan: StarvationPlan
    ) = withContext(dispatcher) {
        appDatabase.starvationTrackQueries.insert(
            id = id,
            startTime = startTime,
            duration = duration,
            starvationPlanId = starvationPlanIdMapper.mapToId(starvationPlan),
            interruptedTimeInMillis = null
        )
    }
}