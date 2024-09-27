package hardcoder.dev.entities.features.foodTracking

import hardcoder.dev.icons.Icon

data class FoodType(
    val id: Int,
    val name: String,
    val icon: Icon,
    val isSpicy: Boolean,
    val isVegetarian: Boolean,
    val proteins: Float,
    val fats: Float,
    val carbohydrates: Float,
)