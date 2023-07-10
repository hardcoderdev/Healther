package hardcoder.dev.uikit.components.button.circleIconButton

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

sealed class CircleIconButtonConfig {
    data class Filled(
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit,
        val iconResId: Int,
        @StringRes val contentDescription: Int? = null,
    ) : CircleIconButtonConfig()

    data class Outlined(
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit,
        val iconResId: Int,
        @StringRes val contentDescription: Int? = null,
    ) : CircleIconButtonConfig()
}