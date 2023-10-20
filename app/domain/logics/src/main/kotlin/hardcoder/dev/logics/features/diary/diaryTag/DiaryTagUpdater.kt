package hardcoder.dev.logics.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import hardcoder.dev.database.entities.features.diary.DiaryTag
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import kotlinx.coroutines.withContext

class DiaryTagUpdater(
    private val diaryTagDao: DiaryTagDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectDiaryTagName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        diaryTagDao.update(
            DiaryTag(
                id = id,
                name = name.data,
                iconId = icon.id,
            )
        )
    }
}