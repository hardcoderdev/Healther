package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.AppPreference
import hardcoder.dev.database.columnAdapters.InstantAdapter

object AppPreferenceAdapters {

    fun createAppPreferenceAdapter() = AppPreference.Adapter(
        firstLaunchTimeAdapter = InstantAdapter,
        lastAppReviewRequestTimeAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
        lastEntranceDateTimeAdapter = InstantAdapter,
    )
}
