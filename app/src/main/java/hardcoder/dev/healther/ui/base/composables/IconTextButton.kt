package hardcoder.dev.healther.ui.base.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import hardcoder.dev.healther.R

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    iconResourceId: ImageVector,
    @StringRes labelResId: Int,
    onClick: () -> Unit,
) {
    Button(onClick = { onClick() }, modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = labelResId),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(2f)
        )
        Icon(
            imageVector = iconResourceId,
            contentDescription = stringResource(id = R.string.start_label_cd),
            modifier = Modifier.weight(0.3f)
        )
    }
}