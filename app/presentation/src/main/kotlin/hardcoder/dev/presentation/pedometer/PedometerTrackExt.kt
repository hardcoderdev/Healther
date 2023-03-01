package hardcoder.dev.presentation.pedometer

import hardcoder.dev.entities.pedometer.PedometerTrack

fun PedometerTrack.toItem(kilometersCount: Float, caloriesBurnt: Float) = PedometerTrackItem(
    stepsCount = stepsCount,
    kilometersCount = kilometersCount,
    caloriesBurnt = caloriesBurnt
)