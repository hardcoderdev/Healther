package hardcoder.dev.uikit.components.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed class CardConfig {
    data class Action(
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit,
        val cardContent: @Composable () -> Unit,
    ) : CardConfig()

    data class Selection(
        val modifier: Modifier = Modifier,
        val onSelect: () -> Unit,
        val isSelected: Boolean,
        val cardContent: @Composable () -> Unit,
    ) : CardConfig()

    data class Static(
        val cardContent: @Composable () -> Unit,
    ) : CardConfig()
}