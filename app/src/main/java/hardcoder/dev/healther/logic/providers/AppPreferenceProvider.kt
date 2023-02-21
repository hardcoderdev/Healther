package hardcoder.dev.healther.logic.providers

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.database.AppPreference
import kotlinx.coroutines.flow.map

class AppPreferenceProvider(private val appDatabase: AppDatabase) {

    fun provideAppPreference() = appDatabase.appPreferenceQueries
        .selectPreferences()
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun AppPreference.toEntity() = hardcoder.dev.healther.entities.AppPreference(
        id, isFirstLaunch
    )
}