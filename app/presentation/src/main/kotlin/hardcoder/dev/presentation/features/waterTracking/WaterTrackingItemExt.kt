package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.entities.features.waterTracking.WaterTrack

fun WaterTrack.toItem(resolvedMillilitersCount: Int) = WaterTrackingItem(
    id = id,
    timeInMillis = date.toEpochMilliseconds(),
    drinkType = drinkType,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount,
)