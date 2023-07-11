package hardcoder.dev.logic.dailyStreak

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.DateTimeProvider
import kotlinx.coroutines.withContext

class DailyStreakUpdater(
    private val appDatabase: AppDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    suspend fun breakStreak() = withContext(dispatchers.io) {
        appDatabase.dailyStreakQueries.breakStreak(
            breakdownDate = dateTimeProvider.currentInstant(),
        )
    }

    suspend fun completeStreak() = withContext(dispatchers.io) {
        appDatabase.dailyStreakQueries.completeStreak(
            completionDate = dateTimeProvider.currentInstant(),
        )
    }
}