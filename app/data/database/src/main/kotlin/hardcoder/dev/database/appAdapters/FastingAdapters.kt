package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.FastingTrack
import hardcoder.dev.database.columnAdapters.InstantAdapter

object FastingAdapters {

    fun createFastingTrackAdapter() = FastingTrack.Adapter(
        startTimeAdapter = InstantAdapter,
        interruptedTimeAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
        fastingPlanIdAdapter = IntColumnAdapter,
    )
}