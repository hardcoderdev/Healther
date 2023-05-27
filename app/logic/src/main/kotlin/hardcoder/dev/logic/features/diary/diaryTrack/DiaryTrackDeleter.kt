package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteById(id: Int) = withContext(dispatcher) {
        appDatabase.diaryTrackQueries.delete(id)
        appDatabase.diaryAttachmentQueries.deleteByDiaryTrackId(id)
    }
}