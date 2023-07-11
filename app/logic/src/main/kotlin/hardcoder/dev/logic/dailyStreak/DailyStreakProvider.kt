package hardcoder.dev.logic.dailyStreak

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DailyStreak
import hardcoder.dev.sqldelight.asFlowOfList
import hardcoder.dev.logic.dailyStreak.DailyStreak as DailyStreakEntity

class DailyStreakProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllStreaks() = appDatabase.dailyStreakQueries
        .provideAllDailyStreaks()
        .asFlowOfList(dispatchers.io) { dailyStreakDatabase ->
            dailyStreakDatabase.toEntity()
        }

    private fun DailyStreak.toEntity() = DailyStreakEntity(
        id = id,
        startDate = startDate,
        breakdownDate = breakdownDate,
        completionDate = completionDate,
    )
}