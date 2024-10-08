package hardcoder.dev.uikit.components.button.internal.requestButton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButton
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@Composable
internal fun <T : RequestController> FilledRequestButtonWithIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    @StringRes labelResId: Int,
    formatArgs: List<Any> = emptyList(),
    controller: T,
    sideEffect: (() -> Unit)? = null,
) {
    val state by controller.state.collectAsState()

    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Filled(
            modifier = modifier,
            iconResId = iconResId,
            labelResId = labelResId,
            formatArgs = formatArgs,
            onClick = {
                controller.request()
                sideEffect?.invoke()
            },
            isEnabled = state.isRequestAllowed,
        ),
    )
}

@OptIn(DelicateCoroutinesApi::class)
@HealtherUiKitPreview
@Composable
internal fun FilledRequestButtonWithIconPreview() {
    HealtherThemePreview {
        FilledRequestButtonWithIcon(
            labelResId = R.string.placeholder_label,
            iconResId = R.drawable.ic_add,
            controller = RequestController(
                coroutineScope = GlobalScope,
                request = {},
            ),
        )
    }
}