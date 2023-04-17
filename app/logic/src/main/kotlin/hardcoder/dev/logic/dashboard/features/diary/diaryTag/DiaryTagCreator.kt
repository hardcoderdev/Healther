package hardcoder.dev.logic.dashboard.features.diary.diaryTag

import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiaryTagCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: String,
        iconId: Long
    ) = withContext(dispatcher) {
        appDatabase.diaryTagQueries.create(
            id = idGenerator.nextId(),
            name = name,
            iconId = iconId
        )
    }
}