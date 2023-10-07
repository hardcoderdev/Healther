package hardcoder.dev.androidApp.ui.screens.user

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hardcoder.dev.logic.user.gender.Gender

data class GenderResources(
    @StringRes val nameResId: Int,
    @DrawableRes val imageResId: Int,
    val gender: Gender,
)