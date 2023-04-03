package hardcoder.dev.presentation.features.waterTracking

import hardcoder.dev.logic.entities.features.waterTracking.WaterTrack

fun WaterTrack.toItem(resolvedMillilitersCount: Int) = WaterTrackItem(
    id = id,
    timeInMillis = date.toEpochMilliseconds(),
    drinkType = drinkType,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount
)