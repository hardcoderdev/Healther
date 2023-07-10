package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.DrinkType
import hardcoder.dev.database.WaterTrack
import hardcoder.dev.database.columnAdapters.InstantAdapter

object WaterTrackingAdapters {

    fun createWaterTrackAdapter() = WaterTrack.Adapter(
        dateAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
        drinkTypeIdAdapter = IntColumnAdapter,
        millilitersCountAdapter = IntColumnAdapter,
    )

    fun createDrinkTypeAdapter() = DrinkType.Adapter(
        idAdapter = IntColumnAdapter,
        iconIdAdapter = IntColumnAdapter,
        hydrationIndexPercentageAdapter = IntColumnAdapter,
    )
}