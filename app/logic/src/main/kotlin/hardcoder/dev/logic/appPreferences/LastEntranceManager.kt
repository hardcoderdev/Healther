package hardcoder.dev.logic.appPreferences

import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.datetime.toLocalDateTime
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

class LastEntranceManager(
    private val appPreferenceProvider: AppPreferenceProvider,
    private val appPreferenceUpdater: AppPreferenceUpdater,
    private val dateTimeProvider: DateTimeProvider,
) {
    private val appPreference by lazy {
        appPreferenceProvider.provideAppPreference()
    }

    private val lastEntranceDate by lazy {
        appPreference.mapNotNull { it?.lastEntranceDateTime?.toLocalDateTime() }
    }

    suspend fun updateLastEntranceDate() {
        val previousAppPreference = appPreference.firstNotNull()
        appPreferenceUpdater.update(
            appPreference = AppPreference(
                firstLaunchTime = previousAppPreference.firstLaunchTime,
                lastAppReviewRequestTime = previousAppPreference.lastAppReviewRequestTime,
                lastEntranceDateTime = dateTimeProvider.currentInstant(),
            ),
        )
    }

    suspend fun calculateEntranceDifferenceInDays(): Int {
        val currentInstant = dateTimeProvider.currentDateFlow().first().getStartOfDay()
        val lastEntranceInstant = lastEntranceDate.first().date.getStartOfDay()
        return (currentInstant - lastEntranceInstant).inWholeDays.toInt()
    }
}