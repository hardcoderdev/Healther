package hardcoder.dev.uikit.icons

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.buttons.ButtonStyles

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    style: ButtonStyles = ButtonStyles.FILLED,
    iconResId: Int,
    @StringRes contentDescription: Int? = null
) {
    when (style) {
        ButtonStyles.FILLED -> {
            androidx.compose.material3.IconButton(
                modifier = modifier.background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
                onClick = onClick
            ) {
                androidx.compose.material3.Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(id = iconResId),
                    contentDescription = contentDescription?.let { stringResource(id = it) },
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        ButtonStyles.OUTLINED -> {
            androidx.compose.material3.IconButton(
                modifier = modifier.border(
                    width = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.onBackground
                ),
                onClick = onClick
            ) {
                androidx.compose.material3.Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(id = iconResId),
                    contentDescription = contentDescription?.let { stringResource(id = it) },
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}