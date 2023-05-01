package hardcoder.dev.uikit.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.modifiers.conditional

@Composable
fun <T> Card(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    interactionType: InteractionType,
    selectedItem: T? = null,
    item: T? = null,
    cardContent: @Composable () -> Unit
) {
    val selectedBorder = if (
        interactionType == InteractionType.SELECTION
        && item == selectedItem
    ) {
        BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        BorderStroke(
            width = 0.dp,
            color = Color.Transparent
        )
    }

    androidx.compose.material3.Card(
        modifier = modifier
            .border(selectedBorder, RoundedCornerShape(16.dp))
            .conditional(onClick != null && interactionType == InteractionType.STATIC) {
                clip(RoundedCornerShape(16.dp))
            }
            .clickable(
                enabled = onClick != null,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
            ) {
                onClick?.invoke()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        cardContent()
    }
}