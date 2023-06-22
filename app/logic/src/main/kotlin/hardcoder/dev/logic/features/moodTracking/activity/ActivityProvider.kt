package hardcoder.dev.logic.features.moodTracking.activity

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.Activity
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.moodTracking.activity.Activity as ActivityEntity

class ActivityProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers
) {

    fun provideAllActivities() = appDatabase.activityQueries
        .provideAllActivities()
        .asFlowOfList(dispatchers.io) { activityDatabase ->
            activityDatabase.toEntity()
        }

    fun provideActivityById(id: Int) = appDatabase.activityQueries
        .provideActivityById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun Activity.toEntity() = ActivityEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId)
    )
}