@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.waterBalance.drinkType

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun DrinkTypeManageScreen(
    onGoBack: () -> Unit,
    onCreateDrinkType: () -> Unit,
    onUpdateDrinkType: (DrinkType) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDrinkTypeViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            DrinkTypeManageContent(
                state = state.value,
                onUpdateDrinkType = onUpdateDrinkType
            )
        },
        onFabClick = onCreateDrinkType,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.drinkTypeManage_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun DrinkTypeManageContent(
    state: DrinkTypeViewModel.State,
    onUpdateDrinkType: (DrinkType) -> Unit
) {
    if (state.drinkTypeList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            maxItemsInEachRow = 4
        ) {
            state.drinkTypeList.forEach { drinkType ->
                DrinkTypeItem(
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                    drinkType = drinkType,
                    selectedDrinkType = null,
                    onSelect = onUpdateDrinkType
                )
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EmptySection(emptyTitleResId = R.string.waterTracking_nowEmpty_text)
        }
    }
}