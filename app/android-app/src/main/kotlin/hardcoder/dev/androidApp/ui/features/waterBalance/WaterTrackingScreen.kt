package hardcoder.dev.androidApp.ui.features.waterBalance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.WaterTrackItem
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Text
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
                        onUpdateWaterTrack = onUpdateWaterTrack
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
    state: WaterTrackingViewModel.State
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(
                id = R.string.waterTracking_millilitersCount_format,
                state.millisCount,
                state.dailyWaterIntake
            ),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = state.millisCount safeDiv state.dailyWaterIntake,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .height(16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))
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
                    onUpdate = onUpdateWaterTrack
                )
            }
        }
    }
}

