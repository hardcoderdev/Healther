package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.DailyStreak
import hardcoder.dev.database.columnAdapters.InstantAdapter

object DailyStreakAdapter {

    fun createDailyStreakAdapter() = DailyStreak.Adapter(
        idAdapter = IntColumnAdapter,
        startDateAdapter = InstantAdapter,
        breakdownDateAdapter = InstantAdapter,
        completionDateAdapter = InstantAdapter,
    )
}