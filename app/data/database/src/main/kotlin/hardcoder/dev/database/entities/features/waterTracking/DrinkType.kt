package hardcoder.dev.database.entities.features.waterTracking

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_types")
data class DrinkType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconId: Int,
    val hydrationIndexInPercentage: Int,
)
