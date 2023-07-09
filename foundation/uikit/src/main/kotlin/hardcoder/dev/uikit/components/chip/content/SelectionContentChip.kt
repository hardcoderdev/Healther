package hardcoder.dev.uikit.components.chip.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SelectionContentChip(
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    isSelected: Boolean = false,
    padding: PaddingValues = PaddingValues(16.dp),
    shape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 16.dp),
    chipContent: @Composable () -> Unit,
) {
    val selectedBorder = if (isSelected) {
        BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        BorderStroke(
            width = 0.dp,
            color = Color.Transparent,
        )
    }

    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = shape)
            .border(selectedBorder, shape)
            .clip(shape)
            .clickable(enabled = true, role = Role.Switch) {
                onSelect()
            }
            .padding(padding)
            .wrapContentWidth()
            .height(15.dp),
    ) {
        chipContent()
    }
}