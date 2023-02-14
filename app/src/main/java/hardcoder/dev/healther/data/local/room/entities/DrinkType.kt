package hardcoder.dev.healther.data.local.room.entities

import androidx.annotation.StringRes
import hardcoder.dev.healther.R

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