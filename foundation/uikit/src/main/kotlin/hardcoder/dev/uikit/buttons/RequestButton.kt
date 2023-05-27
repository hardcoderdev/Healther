package hardcoder.dev.uikit.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.SingleRequestController

@Composable
fun RequestButtonWithIcon(
    controller: SingleRequestController,
    modifier: Modifier = Modifier,
    style: ButtonStyles = ButtonStyles.FILLED,
    @DrawableRes iconResId: Int,
    @StringRes labelResId: Int,
) {
    val state by controller.state.collectAsState()

    ButtonWithIcon(
        modifier = modifier,
        style = style,
        iconResId = iconResId,
        labelResId = labelResId,
        onClick = controller::request,
        isEnabled = state.isRequestAllowed
    )
}