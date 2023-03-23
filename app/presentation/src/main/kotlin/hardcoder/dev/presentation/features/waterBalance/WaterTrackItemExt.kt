package hardcoder.dev.presentation.features.waterBalance

import hardcoder.dev.entities.features.waterTracking.WaterTrack

fun WaterTrack.toItem(resolvedMillilitersCount: Int) = WaterTrackItem(
    id = id,
    timeInMillis = date,
    drinkType = drinkType,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount
)