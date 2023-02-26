package hardcoder.dev.presentation.pedometer

import hardcoder.dev.entities.pedometer.PedometerTrack

fun PedometerTrack.toItem() = PedometerTrackItem(
    id = id,
    stepsCount = stepsCount,
    range = range
)