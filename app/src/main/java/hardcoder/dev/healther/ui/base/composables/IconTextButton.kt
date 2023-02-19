package hardcoder.dev.healther.ui.base.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

enum class ButtonStyles { FILLED, OUTLINED }

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    style: ButtonStyles = ButtonStyles.FILLED,
    iconResourceId: ImageVector,
    @StringRes labelResId: Int,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    @StringRes contentDescription: Int? = null
) {
    when (style) {
        ButtonStyles.FILLED -> {
            Button(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                IconButtonContent(
                    labelResId = labelResId,
                    iconResourceId = iconResourceId,
                    contentDescription = contentDescription
                )
            }
        }

        ButtonStyles.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                IconButtonContent(
                    labelResId = labelResId,
                    iconResourceId = iconResourceId,
                    contentDescription = contentDescription
                )
            }
        }
    }

}

@Composable
private fun RowScope.IconButtonContent(
    @StringRes labelResId: Int,
    iconResourceId: ImageVector,
    @StringRes contentDescription: Int? = null
) {
    Text(
        text = stringResource(id = labelResId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.weight(2f)
    )
    Icon(
        imageVector = iconResourceId,
        contentDescription = contentDescription?.let { stringResource(id = it) },
        modifier = Modifier.weight(0.3f)
    )
}