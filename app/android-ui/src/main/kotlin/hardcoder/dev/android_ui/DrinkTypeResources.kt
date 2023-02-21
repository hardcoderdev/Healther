package hardcoder.dev.android_ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hardcoder.dev.entities.DrinkType

data class DrinkTypeResources(
    @StringRes val title: Int,
    @DrawableRes val image: Int,
    val drinkType: DrinkType
)
