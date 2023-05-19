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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.text.Text

@Composable
fun SelectionChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean = false,
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

    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = shape)
            .border(selectedBorder, shape)
            .clip(shape)
            .clickable(enabled = true, role = Role.Switch) {
                onClick()
            }
            .padding(padding)
            .wrapContentWidth()
            .height(15.dp)
    ) {
        iconResId?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = overflow,
            fontWeight = FontWeight.Bold
        )
    }
}