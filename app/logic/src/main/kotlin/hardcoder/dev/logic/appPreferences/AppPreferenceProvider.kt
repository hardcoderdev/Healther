package hardcoder.dev.logic.appPreferences

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.AppPreference
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.AppPreference as AppPreferenceEntity

class AppPreferenceProvider(private val appDatabase: AppDatabase) {

    fun provideAppPreference() = appDatabase.appPreferenceQueries
        .selectPreferences()
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun AppPreference.toEntity() = AppPreferenceEntity(firstLaunchTime)
}