package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagUpdater(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun update(
        id: Int,
        name: CorrectDiaryTagName,
        icon: LocalIcon
    ) = withContext(ioDispatcher) {
        appDatabase.diaryTagQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id
        )
    }
}