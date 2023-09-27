package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.Experience
import hardcoder.dev.database.columnAdapters.InstantAdapter

object ExperienceAdapters {

    fun createExperienceAdapter() = Experience.Adapter(
        idAdapter = IntColumnAdapter,
        featureTypeIdAdapter = IntColumnAdapter,
        linkedTrackIdAdapter = IntColumnAdapter,
        dateAdapter = InstantAdapter,
        experiencePointsAmountAdapter = FloatColumnAdapter,
    )
}