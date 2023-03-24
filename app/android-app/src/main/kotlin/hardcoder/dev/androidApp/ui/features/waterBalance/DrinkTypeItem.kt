package hardcoder.dev.androidApp.ui.features.waterBalance

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.LocalIconResolver
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.uikit.Chip

@Composable
fun DrinkTypeItem(
    modifier: Modifier = Modifier,
    drinkType: DrinkType,
    selectedDrinkType: DrinkType?,
    onSelect: (DrinkType) -> Unit
) {
    val iconResolver = LocalIconResolver.current

    Chip(
        modifier = modifier,
        onClick = { onSelect(drinkType) },
        text = drinkType.name,
        iconResId = iconResolver.toResourceId(drinkType.iconResourceName),
        shape = RoundedCornerShape(32.dp),
        selectedItem = selectedDrinkType,
        item = drinkType
    )
}