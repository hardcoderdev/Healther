package hardcoder.dev.logic.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class DiaryTrackDeleter(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        appDatabase.diaryTrackQueries.delete(id)
        appDatabase.diaryAttachmentQueries.deleteByDiaryTrackId(id)
    }
}