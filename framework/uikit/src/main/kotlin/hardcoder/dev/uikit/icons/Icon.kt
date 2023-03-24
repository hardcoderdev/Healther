package hardcoder.dev.uikit.icons

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String? = null
) {
    androidx.compose.material.Icon(
        modifier = modifier,
        painter = painterResource(id = iconResId),
        contentDescription = contentDescription
    )
}