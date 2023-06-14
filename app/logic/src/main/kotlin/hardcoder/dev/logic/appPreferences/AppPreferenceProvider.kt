package hardcoder.dev.logic.appPreferences

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.AppPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.appPreferences.AppPreference as AppPreferenceEntity

class AppPreferenceProvider(
    private val appDatabase: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideAppPreference() = appDatabase.appPreferenceQueries
        .providePreferences()
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(ioDispatcher)

    private fun AppPreference.toEntity() = AppPreferenceEntity(
        firstLaunchTime = firstLaunchTime,
        lastAppReviewRequestTime = lastAppReviewRequestTime
    )
}