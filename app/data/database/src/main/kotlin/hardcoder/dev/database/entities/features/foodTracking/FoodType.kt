package hardcoder.dev.database.entities.features.foodTracking

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_types")
data class FoodType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconId: Int,
)
