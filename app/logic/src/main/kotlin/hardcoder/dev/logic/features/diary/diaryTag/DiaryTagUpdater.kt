package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.withContext

class DiaryTagUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectDiaryTagName,
        icon: LocalIcon,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryTagQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
        )
    }
}