package hardcoder.dev.androidApp.ui.features.waterBalance.waterTrack

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
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.WaterTrackItem
import hardcoder.dev.presentation.features.waterBalance.WaterTrackingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.progressBar.LinearProgressBar
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title

@Composable
fun WaterTrackingScreen(
    onGoBack: () -> Unit,
    onHistoryDetails: () -> Unit,
    onSaveWaterTrack: () -> Unit,
    onUpdateWaterTrack: (WaterTrackItem) -> Unit
) {
    val presentationModule = LocalPresentationModule.current

    val viewModel = viewModel {
        presentationModule.getWaterTrackingViewModel()
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
                            iconResId = R.drawable.ic_history,
                            onActionClick = onHistoryDetails
                        )
                    )
                ),
                topBarConfig = TopBarConfig(
                    type = TopBarType.TopBarWithNavigationBack(
                        titleResId = R.string.waterTracking_title_topBar,
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
        Headline(
            text = stringResource(
                id = R.string.waterTracking_millilitersCount_formatText,
                formatArgs = arrayOf(
                    state.millisCount,
                    state.dailyWaterIntake
                )
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressBar(progress = state.millisCount safeDiv state.dailyWaterIntake)
        Spacer(modifier = Modifier.height(32.dp))
        if (state.waterTracks.isNotEmpty()) {
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
        } else {
            Title(text = stringResource(id = R.string.waterTracking_nowEmpty_text))
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_shake_box))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
            Spacer(modifier = Modifier.height(16.dp))
            LottieAnimation(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                composition = composition,
                progress = { progress },
            )
        }
    }
}

