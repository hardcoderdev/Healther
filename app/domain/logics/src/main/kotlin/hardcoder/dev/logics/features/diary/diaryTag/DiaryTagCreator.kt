package hardcoder.dev.logics.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import hardcoder.dev.database.entities.features.diary.DiaryTag
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import kotlinx.coroutines.withContext

class DiaryTagCreator(
    private val diaryTagDao: DiaryTagDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectDiaryTagName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        diaryTagDao.insert(
            DiaryTag(
                name = name.data,
                iconId = icon.id,
            )
        )
    }
}