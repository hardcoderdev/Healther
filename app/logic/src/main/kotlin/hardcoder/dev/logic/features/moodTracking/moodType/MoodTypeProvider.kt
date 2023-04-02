package hardcoder.dev.logic.features.moodTracking.moodType

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.MoodType
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.MoodType as MoodTypeEntity

class MoodTypeProvider(
    private val appDatabase: AppDatabase
) {

    fun provideAllMoodTypes() = appDatabase.moodTypeQueries.provideAllMoodTypes()
        .asFlow()
        .map {
            it.executeAsList().map { moodTypeDatabase ->
                moodTypeDatabase.toEntity()
            }
        }

    fun provideMoodTypeByTrackId(id: Int) = appDatabase.moodTypeQueries.provideMoodTypeById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun MoodType.toEntity() = MoodTypeEntity(
        id = id,
        name = name,
        iconResourceName = iconResourceName,
        positivePercentage = positivePercentage
    )
}