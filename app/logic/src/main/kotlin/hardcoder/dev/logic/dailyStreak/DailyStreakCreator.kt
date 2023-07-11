package hardcoder.dev.logic.dailyStreak

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.datetime.DateTimeProvider
import kotlinx.coroutines.withContext

class DailyStreakCreator(
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun create() = withContext(dispatchers.io) {
        appDatabase.dailyStreakQueries.insert(
            id = idGenerator.nextId(),
            startDate = dateTimeProvider.currentInstant(),
            breakdownDate = null,
            completionDate = null,
        )
    }
}