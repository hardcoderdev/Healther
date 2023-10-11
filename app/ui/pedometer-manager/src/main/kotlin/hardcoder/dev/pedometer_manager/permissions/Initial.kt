package hardcoder.dev.pedometer_manager.permissions

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class Initial(
    @StringRes val titleResId: Int,
    @RawRes val animationResId: Int,
)