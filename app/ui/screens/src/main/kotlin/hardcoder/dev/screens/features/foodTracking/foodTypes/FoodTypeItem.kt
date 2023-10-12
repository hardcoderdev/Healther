package hardcoder.dev.screens.features.foodTracking.foodTypes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.dataProviders.features.FoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme

@Composable
fun FoodTypeItem(
    modifier: Modifier = Modifier,
    foodType: FoodType,
    selectedFoodType: FoodType?,
    onSelect: (FoodType) -> Unit,
) {
    Chip(
        chipConfig = ChipConfig.Selection(
            modifier = modifier,
            onSelect = { onSelect(foodType) },
            text = foodType.name,
            iconResId = foodType.icon.resourceId,
            shape = RoundedCornerShape(32.dp),
            isSelected = foodType == selectedFoodType,
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTypeItemPreview() {
    HealtherTheme {
        FoodTypeItem(
            onSelect = {},
            selectedFoodType = null,
            foodType = FoodTrackingMockDataProvider.foodTypesList()[0],
        )
    }
}