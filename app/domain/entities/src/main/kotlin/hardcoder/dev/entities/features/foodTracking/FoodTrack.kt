package hardcoder.dev.entities.features.foodTracking

import kotlinx.datetime.Instant

data class FoodTrack(
    val id: Int,
    val creationInstant: Instant,
    val foodType: FoodType,
    val calories: Int,
)
