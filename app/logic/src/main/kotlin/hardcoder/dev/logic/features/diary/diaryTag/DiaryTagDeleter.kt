package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagDeleter(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        appDatabase.diaryTagQueries.deleteById(id)
    }
}