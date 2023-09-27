package hardcoder.dev.logic.features.moodTracking.moodActivity

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodActivity
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivity as MoodActivityEntity

class MoodActivityProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: hardcoder.dev.icons.IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllActivities() = appDatabase.moodActivityQueries
        .provideAllMoodActivities()
        .asFlowOfList(dispatchers.io) { activityDatabase ->
            activityDatabase.toEntity()
        }

    fun provideActivityById(id: Int) = appDatabase.moodActivityQueries
        .provideMoodActivityById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun MoodActivity.toEntity() = MoodActivityEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}