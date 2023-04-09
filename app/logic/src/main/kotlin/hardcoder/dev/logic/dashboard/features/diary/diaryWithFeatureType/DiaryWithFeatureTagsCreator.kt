package hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryWithFeatureTagsCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        diaryTrackId: Int,
        featureTagId: Int
    ) = withContext(dispatcher) {
        appDatabase.diaryWithFeatureTagQueries.upsert(
            id = idGenerator.nextId(),
            diaryTrackId = diaryTrackId,
            featureTagId = featureTagId
        )
    }
}