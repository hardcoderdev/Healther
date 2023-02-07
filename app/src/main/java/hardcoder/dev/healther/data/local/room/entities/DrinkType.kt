package hardcoder.dev.healther.data.local.room.entities

import androidx.annotation.StringRes
import hardcoder.dev.healther.R

enum class DrinkType(@StringRes val transcriptionResId: Int) {
    WATER(R.string.water_label),
    COFFEE(R.string.coffee_label),
    BEER(R.string.beer_label),
    MILK(R.string.milk_label),
    TEA(R.string.tea_label),
    JUICE(R.string.juice_label),
    SODA(R.string.soda_label),
    SOUP(R.string.soup_label)
}