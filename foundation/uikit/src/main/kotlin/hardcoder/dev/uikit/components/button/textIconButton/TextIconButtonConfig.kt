package hardcoder.dev.uikit.components.button.textIconButton

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

sealed class TextIconButtonConfig {
    data class Filled(
        val modifier: Modifier = Modifier,
        val isEnabled: Boolean = true,
        val onClick: () -> Unit,
        val iconResId: Int,
        @StringRes val labelResId: Int,
        val formatArgs: List<Any> = emptyList(),
        @StringRes val contentDescription: Int? = null,
    ) : TextIconButtonConfig()

    data class Outlined(
        val modifier: Modifier = Modifier,
        val isEnabled: Boolean = true,
        val onClick: () -> Unit,
        val iconResId: Int,
        @StringRes val labelResId: Int,
        val formatArgs: List<Any> = emptyList(),
        @StringRes val contentDescription: Int? = null,
    ) : TextIconButtonConfig()
}