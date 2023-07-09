package hardcoder.dev.uikit.components.chip

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.chip.internal.ActionChip
import hardcoder.dev.uikit.components.chip.internal.ActionChipPreview
import hardcoder.dev.uikit.components.chip.internal.SelectionChip
import hardcoder.dev.uikit.components.chip.internal.SelectionChipPreview
import hardcoder.dev.uikit.components.chip.internal.StaticChip
import hardcoder.dev.uikit.components.chip.internal.StaticChipPreview
import hardcoder.dev.uikit.preview.UiKitPreview

@Composable
fun Chip(chipConfig: ChipConfig) {
    when (chipConfig) {
        is ChipConfig.Action -> {
            ActionChip(
                onClick = chipConfig.onClick,
                text = chipConfig.text,
                modifier = chipConfig.modifier,
                overflow = chipConfig.overflow,
                iconResId = chipConfig.iconResId,
                padding = chipConfig.padding,
                shape = chipConfig.shape,
            )
        }
        is ChipConfig.Selection -> {
            SelectionChip(
                onSelect = chipConfig.onSelect,
                text = chipConfig.text,
                modifier = chipConfig.modifier,
                overflow = chipConfig.overflow,
                iconResId = chipConfig.iconResId,
                padding = chipConfig.padding,
                shape = chipConfig.shape,
                isSelected = chipConfig.isSelected,
            )
        }
        is ChipConfig.Static -> {
            StaticChip(
                text = chipConfig.text,
                modifier = chipConfig.modifier,
                overflow = chipConfig.overflow,
                iconResId = chipConfig.iconResId,
                padding = chipConfig.padding,
                shape = chipConfig.shape,
            )
        }
    }
}

@UiKitPreview
@Composable
private fun ActionChipPreview() {
    ActionChipPreview()
}

@UiKitPreview
@Composable
private fun SelectionChipPreview() {
    SelectionChipPreview()
}

@UiKitPreview
@Composable
private fun StaticChipPreview() {
    StaticChipPreview()
}