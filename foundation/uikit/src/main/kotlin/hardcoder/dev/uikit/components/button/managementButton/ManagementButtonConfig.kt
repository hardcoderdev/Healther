package hardcoder.dev.uikit.components.button.managementButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

sealed class ManagementButtonConfig {
    data class Chip(
        val modifier: Modifier = Modifier,
        @StringRes val titleResId: Int,
        @DrawableRes val iconResId: Int,
        val onClick: () -> Unit,
    ) : ManagementButtonConfig()

    data class Card(
        val modifier: Modifier = Modifier,
        @StringRes val titleResId: Int,
        @DrawableRes val iconResId: Int,
        val onClick: () -> Unit,
    ) : ManagementButtonConfig()
}