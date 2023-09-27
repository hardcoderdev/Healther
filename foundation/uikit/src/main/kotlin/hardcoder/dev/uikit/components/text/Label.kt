package hardcoder.dev.uikit.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
    )
}

@HealtherUiKitPreview
@Composable
private fun LabelPreview() {
    HealtherThemePreview {
        Label(
            text = stringResource(id = R.string.placeholder_label),
        )
    }
}