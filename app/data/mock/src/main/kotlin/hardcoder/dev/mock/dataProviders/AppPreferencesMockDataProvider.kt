package hardcoder.dev.mock.dataProviders

import hardcoder.dev.entities.appPreferences.AppPreference
import kotlinx.datetime.Clock

object AppPreferencesMockDataProvider {

    fun appPreferences() = AppPreference(
        firstLaunchTime = Clock.System.now(),
    )
}