package hardcoder.dev.logics.features.moodTracking.moodType

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodType
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodType as MoodTypeEntity

class MoodTypeProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllMoodTypes() = appDatabase.moodTypeQueries.provideAllMoodTypes()
        .asFlowOfList(dispatchers.io) { moodTypeDatabase ->
            moodTypeDatabase.toEntity()
        }

    fun provideMoodTypeByTrackId(id: Int) = appDatabase.moodTypeQueries.provideMoodTypeById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun MoodType.toEntity() = MoodTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        positivePercentage = positivePercentage,
    )
}