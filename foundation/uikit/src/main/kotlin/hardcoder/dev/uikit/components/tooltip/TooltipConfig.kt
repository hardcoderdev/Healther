@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit.components.tooltip

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed class TooltipConfig {
    data class Plain(
        val modifier: Modifier = Modifier,
        @StringRes val tooltipResId: Int,
        val content: @Composable (TooltipBoxScope.() -> Unit),
    ) : TooltipConfig()

    data class Rich(
        val modifier: Modifier = Modifier,
        @StringRes val titleResId: Int,
        @StringRes val descriptionResId: Int,
        @StringRes val actionResId: Int,
        val content: @Composable (TooltipBoxScope.() -> Unit),
        val onAction: () -> Unit,
    ) : TooltipConfig()
}