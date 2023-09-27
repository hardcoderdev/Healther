package hardcoder.dev.mock.dataProviders

import hardcoder.dev.logic.appPreferences.AppPreference
import kotlinx.datetime.Clock

object AppPreferencesMockDataProvider {

    fun appPreferences() = AppPreference(
        firstLaunchTime = Clock.System.now(),
        lastEntranceDateTime = Clock.System.now(),
        lastAppReviewRequestTime = Clock.System.now(),
    )
}