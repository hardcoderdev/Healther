package hardcoder.dev.androidApp.ui.setUpFlow.gender

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hardcoder.dev.entities.hero.Gender

data class GenderResources(
    @StringRes val nameResId: Int,
    @DrawableRes val imageResId: Int,
    val gender: Gender
)
