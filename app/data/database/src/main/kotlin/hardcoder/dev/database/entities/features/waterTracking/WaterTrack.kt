package hardcoder.dev.database.entities.features.waterTracking

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "water_tracks",
    foreignKeys = [
        ForeignKey(
            entity = DrinkType::class,
            parentColumns = ["id"],
            childColumns = ["drinkTypeId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class WaterTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val creationInstant: Instant,
    val millilitersCount: Int,
    val drinkTypeId: Int,
)
