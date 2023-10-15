package hardcoder.dev.screens.features.waterTracking.drinkType

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.uikit.components.container.FabConfig
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun DrinkTypes(
    drinkTypesLoadingController: LoadingController<List<DrinkType>>,
    onCreateDrinkType: () -> Unit,
    onUpdateDrinkType: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DrinkTypesContent(
                onUpdateDrinkType = onUpdateDrinkType,
                drinkTypesLoadingController = drinkTypesLoadingController,
            )
        },
        fabConfig = FabConfig(onFabClick = onCreateDrinkType),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_drinkTypes_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun DrinkTypesContent(
    onUpdateDrinkType: (Int) -> Unit,
    drinkTypesLoadingController: LoadingController<List<DrinkType>>,
) {
    LoadingContainer(
        controller = drinkTypesLoadingController,
        loadedContent = { drinkTypeList ->
            if (drinkTypeList.isNotEmpty()) {
                DrinkTypesLoadedContent(
                    drinkTypeList = drinkTypeList,
                    onUpdateDrinkType = onUpdateDrinkType,
                )
            } else {
                DrinkTypesEmptyContent()
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DrinkTypesLoadedContent(
    drinkTypeList: List<DrinkType>,
    onUpdateDrinkType: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        maxItemsInEachRow = 4,
    ) {
        drinkTypeList.forEach { drinkType ->
            DrinkTypeItem(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                drinkType = drinkType,
                selectedDrinkType = null,
                onSelect = {
                    onUpdateDrinkType(drinkType.id)
                },
            )
        }
    }
}

@Composable
private fun DrinkTypesEmptyContent() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        EmptySection(emptyTitleResId = R.string.waterTracking_drinkTypes_nowEmpty_text)
    }
}

@Composable
@HealtherScreenPhonePreviews
private fun DrinkTypesLoadedPreview() {
    HealtherTheme {
        DrinkTypes(
            onUpdateDrinkType = {},
            onGoBack = {},
            onCreateDrinkType = {},
            drinkTypesLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.provideDrinkTypesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}