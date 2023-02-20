package hardcoder.dev.healther.ui.base.extensions

import androidx.annotation.DrawableRes
import hardcoder.dev.healther.database.WaterTrack
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackItem

fun WaterTrack.toItem(
    @DrawableRes imageResId: Int,
    resolvedMillilitersCount: Int
) = WaterTrackItem(
    id = id,
    timeInMillis = date,
    drinkNameResId = drinkType.transcriptionResId,
    imageResId = imageResId,
    millilitersCount = millilitersCount,
    resolvedMillilitersCount = resolvedMillilitersCount
)