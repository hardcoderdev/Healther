package hardcoder.dev.logic.dashboard.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        appDatabase.diaryTagQueries.deleteById(id)
    }
}