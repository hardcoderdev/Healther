package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import kotlinx.coroutines.withContext

class DiaryTagUpdater(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun update(
        id: Int,
        name: CorrectDiaryTagName,
        icon: hardcoder.dev.icons.Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryTagQueries.update(
            id = id,
            name = name.data,
            iconId = icon.id,
        )
    }
}