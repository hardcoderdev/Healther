package hardcoder.dev.uikit.components.button.simpleButton

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

sealed class SimpleButtonConfig {
    data class Filled(
        val modifier: Modifier = Modifier,
        @StringRes val labelResId: Int,
        val onClick: () -> Unit,
        val isEnabled: Boolean = true,
    ) : SimpleButtonConfig()

    data class Outlined(
        val modifier: Modifier = Modifier,
        @StringRes val labelResId: Int,
        val onClick: () -> Unit,
        val isEnabled: Boolean = true,
    ) : SimpleButtonConfig()
}