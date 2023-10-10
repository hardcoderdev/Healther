package hardcoder.dev.logics.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.identification.IdGenerator
import hardcoder.dev.icons.Icon
import hardcoder.dev.logic.features.diary.diaryTag.CorrectDiaryTagName
import kotlinx.coroutines.withContext

class DiaryTagCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create(
        name: CorrectDiaryTagName,
        icon: Icon,
    ) = withContext(dispatchers.io) {
        appDatabase.diaryTagQueries.create(
            id = idGenerator.nextId(),
            name = name.data,
            iconId = icon.id,
        )
    }
}