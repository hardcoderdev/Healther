package hardcoder.dev.logics.features.moodTracking.moodType

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.moodTracking.MoodTypeDao
import hardcoder.dev.database.entities.features.moodTracking.MoodType
import hardcoder.dev.icons.IconResourceProvider
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodType as MoodTypeEntity

class MoodTypeProvider(
    private val moodTypeDao: MoodTypeDao,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllMoodTypes() = moodTypeDao
        .provideAllMoodTypes()
        .mapItems { it.toEntity() }
        .flowOn(dispatchers.io)


    fun provideMoodTypeByTrackId(id: Int) = moodTypeDao
        .provideMoodTypeById(id)
        .map { it?.toEntity() }
        .flowOn(dispatchers.io)

    private fun MoodType.toEntity() = MoodTypeEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
        positivePercentage = positivePercentage,
    )
}