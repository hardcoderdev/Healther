package hardcoder.dev.uikit.components.button.managementButton

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.button.internal.managementButton.CardManagementButton
import hardcoder.dev.uikit.components.button.internal.managementButton.CardManagementButtonPreview
import hardcoder.dev.uikit.components.button.internal.managementButton.ChipManagementButton
import hardcoder.dev.uikit.components.button.internal.managementButton.ChipManagementButtonPreview
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview

@Composable
fun ManagementButton(managementButtonConfig: ManagementButtonConfig) {
    when (managementButtonConfig) {
        is ManagementButtonConfig.Card -> {
            CardManagementButton(
                modifier = managementButtonConfig.modifier,
                titleResId = managementButtonConfig.titleResId,
                iconResId = managementButtonConfig.iconResId,
                onManage = managementButtonConfig.onClick,
            )
        }

        is ManagementButtonConfig.Chip -> {
            ChipManagementButton(
                modifier = managementButtonConfig.modifier,
                titleResId = managementButtonConfig.titleResId,
                iconResId = managementButtonConfig.iconResId,
                onManage = managementButtonConfig.onClick,
            )
        }
    }
}

@HealtherUiKitPreview
@Composable
private fun ChipManagementButtonPreview() {
    ChipManagementButtonPreview()
}

@HealtherUiKitPreview
@Composable
private fun CardManagementButtonPreview() {
    CardManagementButtonPreview()
}