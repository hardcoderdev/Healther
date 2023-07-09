package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.PedometerTrack
import hardcoder.dev.database.columnAdapters.InstantAdapter

object PedometerAdapters {

    fun createPedometerTrackAdapter() = PedometerTrack.Adapter(
        startTimeAdapter = InstantAdapter,
        endTimeAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
        stepsCountAdapter = IntColumnAdapter,
    )
}