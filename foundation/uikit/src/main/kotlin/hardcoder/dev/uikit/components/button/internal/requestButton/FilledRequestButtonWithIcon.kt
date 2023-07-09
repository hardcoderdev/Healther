package hardcoder.dev.uikit.components.button.internal.requestButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@Composable
internal fun <T : SingleRequestController> FilledRequestButtonWithIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    @StringRes labelResId: Int,
    controller: T,
) {
    val state by controller.state.collectAsState()

    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Filled(
            modifier = modifier,
            iconResId = iconResId,
            labelResId = labelResId,
            onClick = controller::request,
            isEnabled = state.isRequestAllowed,
        ),
    )
}

@OptIn(DelicateCoroutinesApi::class)
@UiKitPreview
@Composable
internal fun FilledRequestButtonWithIconPreview() {
    HealtherThemePreview {
        FilledRequestButtonWithIcon(
            labelResId = R.string.placeholder_label,
            iconResId = R.drawable.ic_fab_add,
            controller = SingleRequestController(
                coroutineScope = GlobalScope,
                request = {},
            ),
        )
    }
}