package hardcoder.dev.androidApp.ui.screens.features.fasting

import androidx.compose.runtime.Composable
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun FastingInitial(
    viewModel: FastingViewModel,
    onCreateFastingTrack: () -> Unit,
    onGoToFastingHistory: () -> Unit,
    onGoBack: () -> Unit,
) {
    LoadingContainer(
        controller1 = viewModel.fastingStateLoadingController,
        controller2 = viewModel.statisticLoadingController,
        controller3 = viewModel.chartEntriesLoadingController,
        controller4 = viewModel.lastThreeFastingTrackLoadingController,
        loadedContent = { fastingState, statistic, chartEntries, lastThreeFastingTrackList ->
            ScaffoldWrapper(
                content = {
                    when (fastingState) {
                        is FastingViewModel.FastingState.NotFasting -> {
                            NotFasting(
                                onCreateFastingTrack = onCreateFastingTrack,
                                fastingStatistic = statistic,
                                fastingChartEntries = chartEntries,
                                lastFastingTracks = lastThreeFastingTrackList,
                            )
                        }

                        is FastingViewModel.FastingState.Fasting -> {
                            Fasting(
                                state = fastingState,
                                fastingStatistic = statistic,
                                interruptFastingController = viewModel.interruptFastingController,
                            )
                        }

                        is FastingViewModel.FastingState.Finished -> {
                            FinishFasting(
                                state = fastingState,
                                onClose = viewModel.finishFastingController::request,
                                noteInputController = viewModel.noteInputController,
                            )
                        }
                    }
                },
                actionConfig = actionConfig(
                    fastingState = fastingState,
                    onGoToFastingHistory = onGoToFastingHistory,
                ),
                topBarConfig = topBarConfig(
                    fastingState = fastingState,
                    onGoBack = onGoBack,
                ),
            )
        },
    )
}

private fun actionConfig(
    fastingState: FastingViewModel.FastingState,
    onGoToFastingHistory: () -> Unit,
) = if (fastingState is FastingViewModel.FastingState.NotFasting) {
    ActionConfig(
        actions = listOf(
            Action(
                iconResId = R.drawable.ic_history,
                onActionClick = onGoToFastingHistory,
            ),
        ),
    )
} else {
    null
}

@Composable
private fun topBarConfig(
    fastingState: FastingViewModel.FastingState,
    onGoBack: () -> Unit,
) = if (fastingState is FastingViewModel.FastingState.Finished) {
    TopBarConfig(
        type = TopBarType.TitleTopBar(
            titleResId = R.string.fasting_finish_title_topBar,
        ),
    )
} else {
    TopBarConfig(
        type = TopBarType.TopBarWithNavigationBack(
            titleResId = R.string.fasting_title_topBar,
            onGoBack = onGoBack,
        ),
    )
}