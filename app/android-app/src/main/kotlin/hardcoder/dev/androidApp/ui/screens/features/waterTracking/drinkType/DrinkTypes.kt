package hardcoder.dev.androidApp.ui.screens.features.waterTracking.drinkType

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypesViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun DrinkTypes(
    viewModel: DrinkTypesViewModel,
    onCreateDrinkType: () -> Unit,
    onUpdateDrinkType: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DrinkTypesContent(
                onUpdateDrinkType = onUpdateDrinkType,
                drinkTypesLoadingController = viewModel.drinkTypesLoadingController,
            )
        },
        onFabClick = onCreateDrinkType,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_drinkTypes_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DrinkTypesContent(
    onUpdateDrinkType: (Int) -> Unit,
    drinkTypesLoadingController: LoadingController<List<DrinkType>>,
) {
    LoadingContainer(
        controller = drinkTypesLoadingController,
        loadedContent = { drinkTypeList ->
            if (drinkTypeList.isNotEmpty()) {
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
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    EmptySection(emptyTitleResId = R.string.waterTracking_drinkTypes_nowEmpty_text)
                }
            }
        },
    )
}