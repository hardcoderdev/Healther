package hardcoder.dev.mock.dataProviders

import kotlinx.datetime.Clock

object AppPreferencesMockDataProvider {

    fun appPreferences() = hardcoder.dev.entities.appPreferences.AppPreference(
        firstLaunchTime = Clock.System.now(),
    )
}