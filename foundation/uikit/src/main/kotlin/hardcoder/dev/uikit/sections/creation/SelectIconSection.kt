package hardcoder.dev.uikit.sections.creation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.uikit.components.container.SingleCardSelectionHorizontalGrid
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.resources.R

@Composable
fun SelectIconSection(
    @StringRes titleResId: Int,
    iconSelectionController: SingleSelectionController<Icon>,
) {
    Title(text = stringResource(id = titleResId))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionHorizontalGrid(
        modifier = Modifier.height(200.dp),
        controller = iconSelectionController,
        itemContent = { icon, _ ->
            Icon(
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
                iconResId = icon.resourceId,
                contentDescription = stringResource(R.string.waterTracking_drinkTypes_creation_drinkTypeIconContentDescription),
            )
        },
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    )
}

@Preview
@Composable
private fun SelectIconSectionPreview() {
    HealtherThemePreview {
        SelectIconSection(
            titleResId = R.string.waterTracking_drinkTypes_creation_selectIcon_text,
            iconSelectionController = MockControllersProvider.singleSelectionController(
                dataList = IconsMockDataProvider.icons() + IconsMockDataProvider.icons(),
            ),
        )
    }
}
