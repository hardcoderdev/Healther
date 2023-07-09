package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.withContext

class DiaryTagCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectDiaryTagName,
        icon: LocalIcon,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryTagQueries.create(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
        )
    }
}