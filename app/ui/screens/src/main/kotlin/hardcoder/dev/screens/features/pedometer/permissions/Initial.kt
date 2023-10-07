package hardcoder.dev.screens.features.pedometer.permissions

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import hardcoderdev.healther.foundation.uikit.R as uikitR

data class Initial(
    @StringRes val titleResId: Int = uikitR.string.default_nowEmpty_text,
    @RawRes val animationResId: Int = uikitR.raw.empty_shake_box,
)