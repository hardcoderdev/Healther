package hardcoder.dev.uikit.buttons

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    @StringRes labelResId: Int,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled
    ) {
        androidx.compose.material3.Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.titleMedium
        )
    }
}