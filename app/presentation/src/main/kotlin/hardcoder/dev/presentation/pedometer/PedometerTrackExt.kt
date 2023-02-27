package hardcoder.dev.presentation.pedometer

import hardcoder.dev.entities.pedometer.PedometerTrack

fun PedometerTrack.toItem(kilometersCount: Float, caloriesBurnt: Float) = PedometerTrackItem(
    range = range,
    stepsCount = stepsCount,
    kilometersCount = kilometersCount,
    caloriesBurnt = caloriesBurnt
)