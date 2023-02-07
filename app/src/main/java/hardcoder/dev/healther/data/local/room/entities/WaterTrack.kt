package hardcoder.dev.healther.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waterTracks")
data class WaterTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Long,
    val millilitersCount: Int,
    val drinkType: DrinkType
)
