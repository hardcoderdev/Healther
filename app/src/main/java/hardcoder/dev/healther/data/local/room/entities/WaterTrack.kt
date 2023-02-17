package hardcoder.dev.healther.data.local.room.entities

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import hardcoder.dev.healther.R

@Entity(tableName = "waterTracks")
data class WaterTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val millilitersCount: Int,
    val drinkType: DrinkType
)

enum class DrinkType(@StringRes val transcriptionResId: Int) {
    WATER(R.string.waterTrackItem_waterDrinkType_text),
    COFFEE(R.string.waterTrackItem_coffeeDrinkType_text),
    BEER(R.string.waterTrackItem_beerDrinkType_text),
    MILK(R.string.waterTrackItem_milkDrinkType_text),
    TEA(R.string.waterTrackItem_teaDrinkType_text),
    JUICE(R.string.waterTrackItem_juiceDrinkType_text),
    SODA(R.string.waterTrackItem_sodaDrinkType_text),
    SOUP(R.string.waterTrackItem_soupDrinkType_text)
}
