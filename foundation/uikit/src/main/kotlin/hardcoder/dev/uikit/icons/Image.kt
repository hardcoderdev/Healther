package hardcoder.dev.uikit.icons

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun Image(
    modifier: Modifier = Modifier,
    imageResId: Int,
    @StringRes contentDescription: Int? = null
) {
    androidx.compose.foundation.Image(
        modifier = modifier,
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription?.let { stringResource(id = it) }
    )
}