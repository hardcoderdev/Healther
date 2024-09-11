package hardcoder.dev.screens.features.waterTracking.drinkType

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.chip.Chip
import hardcoder.dev.blocks.components.chip.ChipConfig
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.icons.IconImpl
import hardcoder.dev.icons.resourceId
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

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

@HealtherScreenPhonePreviews
@Composable
private fun DrinkTypeItemPreview() {
    HealtherTheme {
        DrinkTypeItem(
            onSelect = {},
            selectedDrinkType = null,
            drinkType = DrinkType(
                id = 0,
                name = stringResource(R.string.predefined_drinkType_name_coffee),
                hydrationIndexPercentage = 50,
                icon = IconImpl(id = 0, resourceId = R.drawable.ic_apps),
            ),
        )
    }
}