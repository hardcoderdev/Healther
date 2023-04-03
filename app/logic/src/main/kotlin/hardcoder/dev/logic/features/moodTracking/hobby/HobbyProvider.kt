package hardcoder.dev.logic.features.moodTracking.hobby

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Hobby
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.entities.features.moodTracking.Hobby as HobbyTrackEntity

class HobbyProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider
) {

    fun provideAllHobbies() = appDatabase.hobbyQueries
        .provideAllHobbyTracks()
        .asFlow()
        .map {
            it.executeAsList().map { hobbyTrackDatabase ->
                hobbyTrackDatabase.toEntity()
            }
        }

    fun provideHobbyById(id: Int) = appDatabase.hobbyQueries
        .provideHobbyById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun Hobby.toEntity() = HobbyTrackEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId)
    )
}