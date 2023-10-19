package hardcoder.dev.database.entities.features.foodTracking

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "food_tracks",
    foreignKeys = [
        ForeignKey(
            entity = FoodType::class,
            parentColumns = ["id"],
            childColumns = ["foodTypeId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class FoodTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val creationInstant: Instant,
    val calories: Int,
    val foodTypeId: Int,
)
