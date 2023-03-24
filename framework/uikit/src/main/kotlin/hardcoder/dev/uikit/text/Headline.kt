package hardcoder.dev.uikit.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Headline(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineMedium
    )
}