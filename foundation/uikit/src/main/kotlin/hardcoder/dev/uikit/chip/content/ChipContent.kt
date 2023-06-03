package hardcoder.dev.uikit.chip.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Text

@Composable
fun ChipIconDefaultContent(
    @DrawableRes iconResId: Int,
    name: String
) {
    Icon(
        iconResId = iconResId,
        contentDescription = name
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ChipDefaultContent(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}