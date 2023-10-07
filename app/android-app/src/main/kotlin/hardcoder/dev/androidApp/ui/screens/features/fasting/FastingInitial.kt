package hardcoder.dev.androidApp.ui.screens.features.fasting

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticResolver
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.FastingMockDataProvider
import hardcoder.dev.presentation.features.fasting.FastingChartData
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun FastingInitial(
    dateTimeFormatter: DateTimeFormatter,
    fastingStatisticResolver: FastingStatisticResolver,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    millisDistanceFormatter: MillisDistanceFormatter,
    noteInputController: InputController<String>,
    interruptFastingController: RequestController,
    fastingStateLoadingController: LoadingController<FastingViewModel.FastingState>,
    fastingStatisticsLoadingController: LoadingController<FastingStatistic?>,
    fastingChartDataLoadingController: LoadingController<FastingChartData>,
    lastThreeFastingTracksLoadingController: LoadingController<List<FastingTrack>>,
    onCreateFastingTrack: () -> Unit,
    onGoToFastingHistory: () -> Unit,
    onGoBack: () -> Unit,
) {
    LoadingContainer(
        controller1 = fastingStateLoadingController,
        controller2 = fastingStatisticsLoadingController,
        controller3 = fastingChartDataLoadingController,
        controller4 = lastThreeFastingTracksLoadingController,
        loadedContent = { fastingState, statistic, chartData, lastThreeFastingTrackList ->
            ScaffoldWrapper(
                content = {
                    when (fastingState) {
                        is FastingViewModel.FastingState.NotFasting -> {
                            NotFasting(
                                millisDistanceFormatter = millisDistanceFormatter,
                                dateTimeFormatter = dateTimeFormatter,
                                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                                onCreateFastingTrack = onCreateFastingTrack,
                                fastingStatistic = statistic,
                                chartData = chartData,
                                lastFastingTracks = lastThreeFastingTrackList,
                                fastingStatisticResolver = fastingStatisticResolver,
                            )
                        }

                        is FastingViewModel.FastingState.Fasting -> {
                            Fasting(
                                state = fastingState,
                                fastingStatisticResolver = fastingStatisticResolver,
                                millisDistanceFormatter = millisDistanceFormatter,
                                dateTimeFormatter = dateTimeFormatter,
                                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                                fastingStatistic = statistic,
                                interruptFastingController = interruptFastingController,
                            )
                        }

                        is FastingViewModel.FastingState.Finished -> {
                            FinishFasting(
                                state = fastingState,
                                millisDistanceFormatter = millisDistanceFormatter,
                                noteInputController = noteInputController,
                                onClose = onGoBack,
                                interruptFastingController = interruptFastingController,
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

@HealtherScreenPhonePreviews
@Composable
private fun FastingInitialPreview() {
    HealtherTheme {
        FastingInitial(
            onGoBack = {},
            onCreateFastingTrack = {},
            onGoToFastingHistory = {},
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            fastingPlanResourcesProvider = FastingPlanResourcesProvider(),
            fastingStatisticResolver = FastingStatisticResolver(context = LocalContext.current),
            millisDistanceFormatter = MillisDistanceFormatter(
                context = LocalContext.current,
                defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
            ),
            interruptFastingController = MockControllersProvider.requestController(),
            noteInputController = MockControllersProvider.inputController(""),
            fastingChartDataLoadingController = MockControllersProvider.loadingController(
                data = FastingMockDataProvider.fastingChartData(),
            ),
            fastingStateLoadingController = MockControllersProvider.loadingController(
                data = FastingMockDataProvider.fastingState(),
            ),
            fastingStatisticsLoadingController = MockControllersProvider.loadingController(
                data = FastingMockDataProvider.fastingStatistics(),
            ),
            lastThreeFastingTracksLoadingController = MockControllersProvider.loadingController(
                data = FastingMockDataProvider.fastingTracksList(),
            ),
        )
    }
}