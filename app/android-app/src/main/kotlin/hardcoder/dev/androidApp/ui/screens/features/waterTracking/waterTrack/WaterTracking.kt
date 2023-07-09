package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.statistic.WaterTrackingStatisticResolver
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Headline
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlin.math.roundToInt
import org.koin.compose.koinInject

@Composable
fun WaterTracking(
    viewModel: WaterTrackingViewModel,
    onCreateWaterTrack: () -> Unit,
    onUpdateWaterTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoBack: () -> Unit,
) {
    val millilitersDrunkState by viewModel.millilitersDrunkLoadingController.state.collectAsState()

    // TODO MAYBE EXTENSION
    val isFabShowing = (millilitersDrunkState as? LoadingController.State.Loaded)?.data?.let {
        it.millilitersDrunkCount < it.dailyWaterIntake
    } ?: false

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                onUpdateWaterTrack = onUpdateWaterTrack,
                waterTracksLoadingController = viewModel.waterTracksLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
                statisticLoadingController = viewModel.statisticLoadingController,
                millilitersDrunkLoadingController = viewModel.millilitersDrunkLoadingController,
                progressController = viewModel.dailyRateProgressController,
            )
        },
        onFabClick = if (isFabShowing) onCreateWaterTrack else null,
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToHistory,
                ),
            ),
        ),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun WaterTrackingContent(
    onUpdateWaterTrack: (Int) -> Unit,
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    statisticLoadingController: LoadingController<WaterTrackingStatistic?>,
    millilitersDrunkLoadingController: LoadingController<MillilitersDrunkToDailyRate>,
    progressController: LoadingController<Float>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        LoadingContainer(
            controller1 = waterTracksLoadingController,
            controller2 = chartEntriesLoadingController,
            controller3 = statisticLoadingController,
            controller4 = millilitersDrunkLoadingController,
            controller5 = progressController,
        ) { waterTracks, chartEntries, statistic, millilitersDrunk, progress ->
            DailyRateSection(millilitersDrunk, progress)
            Spacer(modifier = Modifier.height(32.dp))

            if (chartEntries.isNotEmpty() && statistic != null) {
                WaterTrackingStatisticSection(statistic)
                Spacer(modifier = Modifier.height(32.dp))
                WaterTrackingChartSection(chartEntries)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (waterTracks.isNotEmpty()) {
                TrackDiarySection(
                    waterTracks = waterTracks,
                    onUpdateWaterTrack = { waterTrack ->
                        onUpdateWaterTrack(waterTrack.id)
                    },
                )
            } else {
                EmptySection(emptyTitleResId = R.string.waterTracking_nowEmpty_text)
            }
        }
    }
}

@Composable
private fun DailyRateSection(
    millilitersDrunk: MillilitersDrunkToDailyRate,
    progress: Float,
) {
    Headline(
        text = stringResource(
            id = R.string.waterTracking_millilitersCount_formatText,
            formatArgs = arrayOf(
                millilitersDrunk.millilitersDrunkCount,
                millilitersDrunk.dailyWaterIntake,
            ),
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    LinearProgressBar(progress = progress)
}

@Composable
private fun WaterTrackingChartSection(chartEntries: List<Pair<Int, Int>>) {
    Title(text = stringResource(id = R.string.waterTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(100.dp),
            chartEntries = chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
        )
    } else {
        Description(text = stringResource(id = R.string.pedometer_chartNotEnoughData_text))
    }
}

@Composable
private fun WaterTrackingStatisticSection(waterTrackingStatistic: WaterTrackingStatistic) {
    val waterTrackingStatisticResolver = koinInject<WaterTrackingStatisticResolver>()

    Title(text = stringResource(id = R.string.waterTracking_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = waterTrackingStatisticResolver.resolve(statistic = waterTrackingStatistic))
}

@Composable
private fun ColumnScope.TrackDiarySection(
    waterTracks: List<WaterTrackingItem>,
    onUpdateWaterTrack: (WaterTrackingItem) -> Unit,
) {
    Title(text = stringResource(id = R.string.waterTracking_diary_text))
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(waterTracks) { track ->
            WaterTrackItem(
                waterTrackingItem = track,
                onUpdate = onUpdateWaterTrack,
            )
        }
    }
}