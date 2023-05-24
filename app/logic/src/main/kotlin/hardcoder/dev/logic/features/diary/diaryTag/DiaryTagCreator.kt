package hardcoder.dev.logic.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: String,
        icon: LocalIcon
    ) = withContext(dispatcher) {
        appDatabase.diaryTagQueries.create(
            id = idGenerator.nextId(),
            name = name,
            iconId = icon.id
        )
    }
}