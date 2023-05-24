package hardcoder.dev.uikit.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SimpleButton(
    modifier: Modifier = Modifier,
    style: ButtonStyles = ButtonStyles.FILLED,
    @StringRes labelResId: Int,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    when (style) {
        ButtonStyles.FILLED -> {
            Button(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                SimpleButtonContent(labelResId = labelResId)
            }
        }

        ButtonStyles.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                SimpleButtonContent(labelResId = labelResId)
            }
        }
    }
}

@Composable
private fun SimpleButtonContent(@StringRes labelResId: Int) {
    androidx.compose.material3.Text(
        text = stringResource(id = labelResId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium
    )
}