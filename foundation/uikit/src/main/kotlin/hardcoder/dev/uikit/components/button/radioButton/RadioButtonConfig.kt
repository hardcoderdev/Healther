package hardcoder.dev.uikit.components.button.radioButton

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

sealed class RadioButtonConfig {
    data class Filled(
        val modifier: Modifier = Modifier,
        @StringRes val labelResId: Int,
        val onClick: () -> Unit,
        val isSelected: Boolean,
        val isEnabled: Boolean = true,
    ) : RadioButtonConfig()

    data class Outlined(
        val modifier: Modifier = Modifier,
        @StringRes val labelResId: Int,
        val onClick: () -> Unit,
        val isSelected: Boolean,
        val isEnabled: Boolean = true,
    ) : RadioButtonConfig()
}