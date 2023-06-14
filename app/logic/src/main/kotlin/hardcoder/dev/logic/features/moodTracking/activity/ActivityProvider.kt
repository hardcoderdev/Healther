package hardcoder.dev.logic.features.moodTracking.activity

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.Activity
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.moodTracking.activity.Activity as ActivityEntity

class ActivityProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideAllActivities() = appDatabase.activityQueries
        .provideAllActivities()
        .asFlowOfList(ioDispatcher) { activityDatabase ->
            activityDatabase.toEntity()
        }

    fun provideActivityById(id: Int) = appDatabase.activityQueries
        .provideActivityById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(ioDispatcher)

    private fun Activity.toEntity() = ActivityEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId)
    )
}