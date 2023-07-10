package hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig

@Composable
fun DrinkTypeItem(
    modifier: Modifier = Modifier,
    drinkType: DrinkType,
    selectedDrinkType: DrinkType?,
    onSelect: (DrinkType) -> Unit,
) {
    Chip(
        chipConfig = ChipConfig.Selection(
            modifier = modifier,
            onSelect = { onSelect(drinkType) },
            text = drinkType.name,
            iconResId = drinkType.icon.resourceId,
            shape = RoundedCornerShape(32.dp),
            isSelected = drinkType == selectedDrinkType,
        ),
    )
}