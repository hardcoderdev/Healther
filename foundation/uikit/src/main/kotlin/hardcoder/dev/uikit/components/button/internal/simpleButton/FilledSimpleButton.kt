package hardcoder.dev.uikit.components.button.internal.simpleButton

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material3.Text as MaterialText

@Composable
internal fun FilledSimpleButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    @StringRes labelResId: Int,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
    ) {
        MaterialText(
            text = stringResource(id = labelResId),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@UiKitPreview
@Composable
internal fun FilledSimpleButtonPreview() {
    HealtherThemePreview {
        FilledSimpleButton(
            onClick = { /* no-op */ },
            isEnabled = true,
            labelResId = R.string.placeholder_label,
        )
    }
}