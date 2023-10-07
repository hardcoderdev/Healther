package hardcoder.dev.screens.features.pedometer.permissions

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class Rejected(
    @RawRes val animationResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
)