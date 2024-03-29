package hardcoder.dev.logics.features.moodTracking.moodActivity

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.moodTracking.MoodActivityDao
import hardcoder.dev.database.entities.features.moodTracking.MoodActivity
import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.IconResourceProvider
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
        .mapItems { it.toEntity(icon = iconResourceProvider.getIcon(it.iconId)) }
        .flowOn(dispatchers.io)

    fun provideActivityById(id: Int) = moodActivityDao
        .provideMoodActivityById(id)
        .map { it?.toEntity(icon = iconResourceProvider.getIcon(it.iconId)) }
        .flowOn(dispatchers.io)
}

private fun MoodActivity.toEntity(icon: Icon) = MoodActivityEntity(
    id = id,
    name = name,
    icon = icon,
)