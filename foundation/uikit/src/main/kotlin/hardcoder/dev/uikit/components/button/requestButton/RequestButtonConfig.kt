package hardcoder.dev.uikit.components.button.requestButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.request.SingleRequestController

sealed class RequestButtonConfig<T : SingleRequestController> {
    data class Filled<T : SingleRequestController>(
        val controller: T,
        val modifier: Modifier = Modifier,
        @DrawableRes val iconResId: Int,
        @StringRes val labelResId: Int,
    ) : RequestButtonConfig<T>()

    data class Outlined<T : SingleRequestController>(
        val controller: T,
        val modifier: Modifier = Modifier,
        @DrawableRes val iconResId: Int,
        @StringRes val labelResId: Int,
    ) : RequestButtonConfig<T>()
}