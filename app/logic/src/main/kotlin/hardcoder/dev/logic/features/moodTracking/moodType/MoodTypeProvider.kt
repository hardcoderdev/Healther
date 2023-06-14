package hardcoder.dev.logic.features.moodTracking.moodType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodType
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType as MoodTypeEntity

class MoodTypeProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideAllMoodTypes() = appDatabase.moodTypeQueries.provideAllMoodTypes()
        .asFlowOfList(ioDispatcher) { moodTypeDatabase ->
            moodTypeDatabase.toEntity()
        }

    fun provideMoodTypeByTrackId(id: Int) = appDatabase.moodTypeQueries.provideMoodTypeById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(ioDispatcher)

    private fun MoodType.toEntity() = MoodTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        positivePercentage = positivePercentage
    )
}