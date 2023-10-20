package hardcoder.dev.logics.features.diary.diaryTrack

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryAttachmentDao
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import kotlinx.coroutines.withContext

class DiaryTrackDeleter(
    private val diaryTrackDao: DiaryTrackDao,
    private val diaryAttachmentDao: DiaryAttachmentDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        diaryTrackDao.deleteById(id)
        diaryAttachmentDao.deleteDiaryAttachmentsByDiaryTrackId(id)
    }
}