package hardcoder.dev.uikit.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun Headline(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineMedium,
    )
}

@UiKitPreview
@Composable
private fun HeadlinePreview() {
    HealtherThemePreview {
        Headline(
            text = stringResource(id = R.string.placeholder_label),
        )
    }
}