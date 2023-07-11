package hardcoder.dev.logic.appPreferences

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.AppPreference
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.appPreferences.AppPreference as AppPreferenceEntity

class AppPreferenceProvider(
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAppPreference() = appDatabase.appPreferenceQueries
        .providePreferences()
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun AppPreference.toEntity() = AppPreferenceEntity(
        firstLaunchTime = firstLaunchTime,
        lastAppReviewRequestTime = lastAppReviewRequestTime,
        lastEntranceDateTime = lastEntranceDateTime,
    )
}