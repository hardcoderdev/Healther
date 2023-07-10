package hardcoder.dev.uikit.components.button.requestButton

import androidx.compose.runtime.Composable
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.uikit.components.button.internal.requestButton.FilledRequestButtonWithIcon
import hardcoder.dev.uikit.components.button.internal.requestButton.FilledRequestButtonWithIconPreview
import hardcoder.dev.uikit.components.button.internal.requestButton.OutlinedRequestButtonWithIcon
import hardcoder.dev.uikit.components.button.internal.requestButton.OutlinedRequestButtonWithIconPreview
import hardcoder.dev.uikit.preview.UiKitPreview

@Composable
fun <T : SingleRequestController> RequestButtonWithIcon(requestButtonConfig: RequestButtonConfig<T>) {
    when (requestButtonConfig) {
        is RequestButtonConfig.Filled<T> -> FilledRequestButtonWithIcon(
            modifier = requestButtonConfig.modifier,
            controller = requestButtonConfig.controller,
            iconResId = requestButtonConfig.iconResId,
            labelResId = requestButtonConfig.labelResId,
        )

        is RequestButtonConfig.Outlined<T> -> OutlinedRequestButtonWithIcon<T>(
            modifier = requestButtonConfig.modifier,
            controller = requestButtonConfig.controller,
            iconResId = requestButtonConfig.iconResId,
            labelResId = requestButtonConfig.labelResId,
        )
    }
}

@UiKitPreview
@Composable
private fun FilledRequestButtonWithIconPreview() {
    FilledRequestButtonWithIconPreview()
}

@UiKitPreview
@Composable
private fun OutlinedRequestButtonWithIconPreview() {
    OutlinedRequestButtonWithIconPreview()
}