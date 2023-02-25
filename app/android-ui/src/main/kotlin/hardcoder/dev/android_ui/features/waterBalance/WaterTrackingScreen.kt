package hardcoder.dev.android_ui.features.waterBalance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.waterBalance.WaterTrackItem
import hardcoder.dev.presentation.waterBalance.WaterTrackingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

@Composable
fun WaterTrackingScreen(
    onGoBack: () -> Unit,
    onHistoryDetails: () -> Unit,
    onSaveWaterTrack: () -> Unit,
    onUpdateWaterTrack: (WaterTrackItem) -> Unit
) {
    val presentationModule = LocalPresentationModule.current

    val viewModel = viewModel {
        presentationModule.createWaterTrackingViewModel()
    }
    val state = viewModel.state.collectAsState()

    when (val fetchingState = state.value) {
        is WaterTrackingViewModel.LoadingState.Loaded -> {
            val dailyWaterIntake = fetchingState.state.dailyWaterIntake
            val millisCount = fetchingState.state.millisCount

            ScaffoldWrapper(
                content = {
                    WaterTrackingContent(
                        state = fetchingState.state,
                        onUpdateWaterTrack = onUpdateWaterTrack,
                        onDeleteWaterTrack = viewModel::delete
                    )
                },
                onFabClick = if (millisCount < dailyWaterIntake) onSaveWaterTrack else null,
                actionConfig = ActionConfig(
                    actions = listOf(
                        Action(
                            iconImageVector = Icons.Filled.MoreVert,
                            onActionClick = onHistoryDetails
                        )
                    )
                ),
                topBarConfig = TopBarConfig(
                    type = TopBarType.TopBarWithNavigationBack(
                        titleResId = R.string.waterTrackingFeature_title_topBar,
                        onGoBack = onGoBack
                    )
                )
            )
        }

        is WaterTrackingViewModel.LoadingState.Loading -> {
            /* no-op */
        }
    }
}

@Composable
private fun WaterTrackingContent(
    onUpdateWaterTrack: (WaterTrackItem) -> Unit,
    onDeleteWaterTrack: (Int) -> Unit,
    state: WaterTrackingViewModel.State
) {
    val millisCount = state.millisCount
    val dailyWaterIntake = state.dailyWaterIntake

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        hardcoder.dev.uikit.CircularProgressBar(
            number = state.millisCount,
            percentage = if (millisCount != 0) {
                (millisCount.toFloat() / dailyWaterIntake.toFloat())
                    .toString().substring(0, 3).toFloat()
            } else {
                0.0f
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(state.waterTracks) { track ->
                WaterTrackItem(
                    waterTrackItem = track,
                    onDelete = onDeleteWaterTrack,
                    onUpdate = onUpdateWaterTrack
                )
            }
        }
    }
}

