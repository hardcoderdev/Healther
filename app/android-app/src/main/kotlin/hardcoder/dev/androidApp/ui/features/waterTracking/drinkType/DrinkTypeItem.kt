package hardcoder.dev.androidApp.ui.features.waterTracking.drinkType

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.logic.entities.features.waterTracking.DrinkType
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.chip.Chip

@Composable
fun DrinkTypeItem(
    modifier: Modifier = Modifier,
    interactionType: InteractionType = InteractionType.SELECTION,
    drinkType: DrinkType,
    selectedDrinkType: DrinkType?,
    onSelect: (DrinkType) -> Unit
) {
    Chip(
        modifier = modifier,
        interactionType = interactionType,
        onClick = { onSelect(drinkType) },
        text = drinkType.name,
        iconResId = drinkType.icon.resourceId,
        shape = RoundedCornerShape(32.dp),
        isSelected = drinkType == selectedDrinkType
    )
}