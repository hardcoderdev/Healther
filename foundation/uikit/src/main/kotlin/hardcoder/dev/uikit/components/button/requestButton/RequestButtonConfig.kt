package hardcoder.dev.uikit.components.button.requestButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.request.RequestController

sealed class RequestButtonConfig<T : RequestController> {
    data class Filled<T : RequestController>(
        val controller: T,
        val sideEffect: (() -> Unit)? = null,
        val modifier: Modifier = Modifier,
        @DrawableRes val iconResId: Int,
        @StringRes val labelResId: Int,
        val formatArgs: List<Any> = emptyList(),
    ) : RequestButtonConfig<T>()

    data class Outlined<T : RequestController>(
        val controller: T,
        val sideEffect: (() -> Unit)? = null,
        val modifier: Modifier = Modifier,
        @DrawableRes val iconResId: Int,
        @StringRes val labelResId: Int,
        val formatArgs: List<Any> = emptyList(),
    ) : RequestButtonConfig<T>()
}