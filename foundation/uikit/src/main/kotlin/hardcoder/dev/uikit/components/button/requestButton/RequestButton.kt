package hardcoder.dev.uikit.components.button.requestButton

import androidx.compose.runtime.Composable
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.uikit.components.button.internal.requestButton.FilledRequestButtonWithIcon
import hardcoder.dev.uikit.components.button.internal.requestButton.FilledRequestButtonWithIconPreview
import hardcoder.dev.uikit.components.button.internal.requestButton.OutlinedRequestButtonWithIcon
import hardcoder.dev.uikit.components.button.internal.requestButton.OutlinedRequestButtonWithIconPreview
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview

@Composable
fun <T : RequestController> RequestButtonWithIcon(requestButtonConfig: RequestButtonConfig<T>) {
    when (requestButtonConfig) {
        is RequestButtonConfig.Filled<T> -> FilledRequestButtonWithIcon(
            modifier = requestButtonConfig.modifier,
            controller = requestButtonConfig.controller,
            sideEffect = requestButtonConfig.sideEffect,
            iconResId = requestButtonConfig.iconResId,
            labelResId = requestButtonConfig.labelResId,
            formatArgs = requestButtonConfig.formatArgs,
        )

        is RequestButtonConfig.Outlined<T> -> OutlinedRequestButtonWithIcon(
            modifier = requestButtonConfig.modifier,
            controller = requestButtonConfig.controller,
            sideEffect = requestButtonConfig.sideEffect,
            iconResId = requestButtonConfig.iconResId,
            labelResId = requestButtonConfig.labelResId,
            formatArgs = requestButtonConfig.formatArgs,
        )
    }
}

@HealtherUiKitPreview
@Composable
private fun FilledRequestButtonWithIconPreview() {
    FilledRequestButtonWithIconPreview()
}

@HealtherUiKitPreview
@Composable
private fun OutlinedRequestButtonWithIconPreview() {
    OutlinedRequestButtonWithIconPreview()
}