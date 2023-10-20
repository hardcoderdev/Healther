package hardcoder.dev.logics.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import kotlinx.coroutines.withContext

class DiaryTagDeleter(
    private val diaryTagDao: DiaryTagDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun deleteById(id: Int) = withContext(dispatchers.io) {
        diaryTagDao.deleteById(id)
    }
}