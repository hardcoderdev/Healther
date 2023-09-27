package hardcoder.dev.uikit.components.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        color = MaterialTheme.colorScheme.error,
        text = text,
        imageVector = Icons.Default.Error,
        iconTint = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.titleSmall,
    )
}

@HealtherUiKitPreview
@Composable
private fun ErrorTextPreview() {
    HealtherThemePreview {
        ErrorText(
            text = stringResource(id = R.string.placeholder_label),
        )
    }
}