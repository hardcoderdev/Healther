package hardcoder.dev.presentation.waterBalance

import hardcoder.dev.entities.waterTracking.WaterTrack

fun WaterTrack.toItem(resolvedMillilitersCount: Int) = WaterTrackItem(
    id = id,
    timeInMillis = date,
    drinkType = drinkType,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount
)