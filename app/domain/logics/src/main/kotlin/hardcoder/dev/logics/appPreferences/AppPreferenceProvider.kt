package hardcoder.dev.logics.appPreferences

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.dao.appPreferences.AppPreferencesDao
import hardcoder.dev.database.entities.appPreferences.AppPreferences
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.appPreferences.AppPreference as AppPreferenceEntity

class AppPreferenceProvider(
    private val appPreferencesDao: AppPreferencesDao,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAppPreference() = appPreferencesDao
        .providePreferences()
        .map(AppPreferences::toEntity)
        .flowOn(dispatchers.io)
}

private fun AppPreferences.toEntity() = AppPreferenceEntity(
    firstLaunchTime = firstLaunchTime,
)