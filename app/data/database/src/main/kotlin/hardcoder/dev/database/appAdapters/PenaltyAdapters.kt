package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.Penalty
import hardcoder.dev.database.columnAdapters.InstantAdapter

object PenaltyAdapters {

    fun createPenaltyAdapter() = Penalty.Adapter(
        idAdapter = IntColumnAdapter,
        featureTypeIdAdapter = IntColumnAdapter,
        dateAdapter = InstantAdapter,
        healthPointsAmountAdapter = IntColumnAdapter,
    )
}