package hardcoder.dev.uikit.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

enum class ButtonStyles { FILLED, OUTLINED }

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    style: ButtonStyles = ButtonStyles.FILLED,
    @DrawableRes iconResId: Int,
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
                IconButtonContent(
                    labelResId = labelResId,
                    iconResourceId = iconResId,
                    contentDescription = labelResId,
                    style = ButtonStyles.FILLED
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
                    iconResourceId = iconResId,
                    contentDescription = labelResId,
                    style = ButtonStyles.OUTLINED
                )
            }
        }
    }
}

@Composable
private fun RowScope.IconButtonContent(
    style: ButtonStyles,
    @StringRes labelResId: Int,
    @DrawableRes iconResourceId: Int,
    @StringRes contentDescription: Int
) {
    val color = if (style == ButtonStyles.FILLED) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Text(
        text = stringResource(id = labelResId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.W500,
        modifier = Modifier.weight(2f),
        color = color
    )
    androidx.compose.material.Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = stringResource(id = contentDescription),
        modifier = Modifier.weight(0.3f),
        tint = color
    )
}