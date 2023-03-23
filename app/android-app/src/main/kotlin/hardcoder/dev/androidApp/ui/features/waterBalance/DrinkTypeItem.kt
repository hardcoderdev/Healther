package hardcoder.dev.androidApp.ui.features.waterBalance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val selectedBorder = if (drinkType == selectedDrinkType) BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary
    ) else BorderStroke(
        width = 0.dp,
        color = Color.Transparent
    )

    Chip(
        modifier = modifier,
        onClick = { onSelect(drinkType) },
        text = drinkType.name,
        iconResId = iconResolver.toResourceId(drinkType.iconResourceName),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(32.dp),
        selectedBorderStroke = selectedBorder
    )
}