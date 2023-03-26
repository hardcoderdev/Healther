package hardcoder.dev.uikit.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.text.Text

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    text: String,
    isSelected: Boolean = false,
    interactionType: InteractionType = InteractionType.SELECTION,
    height: Dp = 15.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    @DrawableRes iconResId: Int? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    shape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 16.dp)
) {
    val selectedBorder = if (isSelected) BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary
    ) else BorderStroke(
        width = 0.dp,
        color = Color.Transparent
    )

    val containerColor = when (interactionType) {
        InteractionType.ACTION -> MaterialTheme.colorScheme.primary
        InteractionType.SELECTION -> MaterialTheme.colorScheme.primaryContainer
        InteractionType.STATIC -> MaterialTheme.colorScheme.primary
    }

    val onContainerColor = when (interactionType) {
        InteractionType.ACTION -> MaterialTheme.colorScheme.onPrimary
        InteractionType.SELECTION -> MaterialTheme.colorScheme.onBackground
        InteractionType.STATIC -> MaterialTheme.colorScheme.onPrimary
    }

    Row(
        modifier = modifier
            .background(color = containerColor, shape = shape)
            .border(selectedBorder, shape)
            .clip(shape)
            .clickable(enabled = onClick != null, role = Role.Switch) {
                if (onClick != null) {
                    onClick()
                }
            }
            .padding(padding)
            .wrapContentWidth()
            .height(height)
    ) {
        iconResId?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = onContainerColor
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = textStyle,
            color = onContainerColor,
            overflow = overflow,
            fontWeight = FontWeight.Bold
        )
    }
}