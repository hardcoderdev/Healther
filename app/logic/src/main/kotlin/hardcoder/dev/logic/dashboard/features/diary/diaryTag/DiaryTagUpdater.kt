package hardcoder.dev.logic.dashboard.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagUpdater(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun update(diaryTag: DiaryTag) = withContext(dispatcher) {
        appDatabase.diaryTagQueries.update(
            name = diaryTag.name,
            iconId = diaryTag.icon.id
        )
    }
}