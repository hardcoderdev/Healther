package hardcoder.dev.uikit.components.chip.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Text

@Composable
fun ChipIconDefaultContent(
    @DrawableRes iconResId: Int,
    name: String,
) {
    Icon(
        iconResId = iconResId,
        contentDescription = name,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
    )
}