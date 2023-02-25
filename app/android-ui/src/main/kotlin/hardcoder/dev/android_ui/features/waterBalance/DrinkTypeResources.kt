package hardcoder.dev.android_ui.features.waterBalance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hardcoder.dev.entities.waterTracking.DrinkType

data class DrinkTypeResources(
    @StringRes val title: Int,
    @DrawableRes val image: Int,
    val drinkType: DrinkType
)
