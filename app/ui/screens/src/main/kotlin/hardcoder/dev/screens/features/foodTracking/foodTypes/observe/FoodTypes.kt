package hardcoder.dev.screens.features.foodTracking.foodTypes.observe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.fabButton.FabConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.FoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FoodTypes(
    foodTypesLoadingController: LoadingController<List<FoodType>>,
    onCreateFoodType: () -> Unit,
    onUpdateFoodType: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FoodTypesContent(
                onUpdateFoodType = onUpdateFoodType,
                foodTypesLoadingController = foodTypesLoadingController,
            )
        },
        fabConfig = FabConfig.LargeFab(
            onFabClick = onCreateFoodType,
            iconResId = R.drawable.ic_create,
        ),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.foodTracking_foodTypes_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FoodTypesContent(
    onUpdateFoodType: (Int) -> Unit,
    foodTypesLoadingController: LoadingController<List<FoodType>>,
) {
    LoadingContainer(
        controller = foodTypesLoadingController,
        loadedContent = { foodTypesList ->
            if (foodTypesList.isNotEmpty()) {
                FoodTypesLoadedContent(
                    foodTypesList = foodTypesList,
                    onUpdateFoodType = onUpdateFoodType,
                )
            } else {
                FoodTypesEmptyContent()
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FoodTypesLoadedContent(
    foodTypesList: List<FoodType>,
    onUpdateFoodType: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        maxItemsInEachRow = 4,
    ) {
        foodTypesList.forEach { foodType ->
            FoodTypeItem(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                foodType = foodType,
                selectedFoodType = null,
                onSelect = {
                    onUpdateFoodType(foodType.id)
                },
            )
        }
    }
}

@Composable
private fun FoodTypesEmptyContent() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        EmptySection(emptyTitleResId = R.string.foodTracking_foodTypes_nowEmpty_text)
    }
}

@Composable
@HealtherScreenPhonePreviews
private fun DrinkTypesLoadedPreview() {
    HealtherTheme {
        FoodTypes(
            onGoBack = {},
            onCreateFoodType = {},
            onUpdateFoodType = {},
            foodTypesLoadingController = MockControllersProvider.loadingController(
                data = FoodTrackingMockDataProvider.foodTypesList(),
            ),
        )
    }
}