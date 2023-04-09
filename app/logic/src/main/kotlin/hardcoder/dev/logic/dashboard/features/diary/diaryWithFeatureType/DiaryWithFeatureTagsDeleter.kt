package hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryWithFeatureTagsDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteAllDiaryWithFeatureTagsByDiaryId(id: Int) = withContext(dispatcher) {
        appDatabase.diaryWithFeatureTagQueries.deleteAllFeatureTagsByDiaryTrackId(id)
    }
}