package hardcoder.dev.logic.features.moodTracking.hobby

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.HobbyTrack
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.moodTracking.HobbyTrack as HobbyTrackEntity

class HobbyTrackProvider(private val appDatabase: AppDatabase) {

    fun provideAllHobbies() = appDatabase.hobbyTrackQueries
        .provideAllHobbyTracks()
        .asFlow()
        .map {
            it.executeAsList().map { hobbyTrackDatabase ->
                hobbyTrackDatabase.toEntity()
            }
        }

    fun provideHobbyById(id: Int) = appDatabase.hobbyTrackQueries
        .provideHobbyById(id)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun HobbyTrack.toEntity() = HobbyTrackEntity(
        id, name, iconResourceName
    )
}