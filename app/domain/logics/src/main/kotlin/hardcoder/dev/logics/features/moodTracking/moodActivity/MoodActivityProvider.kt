package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.features.moodTracking.MoodActivityDao
import hardcoder.dev.database.entities.features.moodTracking.MoodActivity
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodActivity as MoodActivityEntity

class MoodActivityProvider(
    private val moodActivityDao: MoodActivityDao,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllActivities() = moodActivityDao
        .provideAllMoodActivities()
        .asFlowOfList(dispatchers.io) { activityDatabase ->
            activityDatabase.toEntity()
        }

    fun provideActivityById(id: Int) = moodActivityDao
        .provideMoodActivityById(id)
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun MoodActivity.toEntity() = MoodActivityEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}