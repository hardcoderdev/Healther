package hardcoder.dev.presentation

import hardcoder.dev.entities.DrinkType
import hardcoder.dev.entities.WaterTrack

fun WaterTrack.toItem(
    drinkType: DrinkType,
    resolvedMillilitersCount: Int
) = WaterTrackItem(
    id = id,
    timeInMillis = date,
    drinkType = drinkType,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount
)