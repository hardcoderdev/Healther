package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.FoodTrack
import hardcoder.dev.database.FoodType
import hardcoder.dev.database.columnAdapters.InstantAdapter

object FoodTrackingAdapters {

    fun createFoodTypeAdapter() = FoodType.Adapter(
        idAdapter = IntColumnAdapter,
        iconIdAdapter = IntColumnAdapter,
        proteinsAdapter = FloatColumnAdapter,
        fatsAdapter = FloatColumnAdapter,
        carbohydratesAdapter = FloatColumnAdapter,
    )

    fun createFoodTrackAdapter() = FoodTrack.Adapter(
        idAdapter = IntColumnAdapter,
        creationInstantAdapter = InstantAdapter,
        foodTypeIdAdapter = IntColumnAdapter,
        caloriesAdapter = IntColumnAdapter,
    )
}