@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.waterBalance.drinkType

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.entities.features.waterTracking.DrinkType
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.text.Title

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
                .padding(16.dp)) {
            Title(text = stringResource(id = R.string.drinkTypeManage_nowEmpty_text))
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_astronaut))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
            Spacer(modifier = Modifier.height(16.dp))
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                composition = composition,
                progress = { progress },
            )
        }
    }
}