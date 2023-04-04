package hardcoder.dev.uikit.card

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Label
import hardcoder.dev.uikit.text.Title

data class CardInfo(
    @DrawableRes val iconResId: Int,
    @StringRes val nameResId: Int,
    val value: String
)

@Composable
fun CardInfoItem(
    @DrawableRes iconResId: Int,
    @StringRes nameResId: Int,
    value: String
) {
    Column {
        Icon(iconResId = iconResId, contentDescription = stringResource(id = nameResId))
        Spacer(modifier = Modifier.height(8.dp))
        Title(text = value)
        Label(text = stringResource(id = nameResId))
    }
}