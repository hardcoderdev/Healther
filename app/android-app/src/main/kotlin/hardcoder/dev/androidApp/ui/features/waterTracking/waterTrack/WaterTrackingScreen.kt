package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack

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
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.charts.ActivityLineChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.progressBar.LinearProgressBar
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title
import kotlin.math.roundToInt

@Composable
fun WaterTrackingScreen(
    onGoBack: () -> Unit,
    onHistoryDetails: () -> Unit,
    onSaveWaterTrack: () -> Unit,
    onUpdateWaterTrack: (WaterTrackingItem) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getWaterTrackingViewModel() }
    val millilitersDrunkState by viewModel.millilitersDrunkLoadingController.state.collectAsState()

    val showFab = (millilitersDrunkState as? LoadingController.State.Loaded)?.data?.let {
        it.millilitersDrunkCount < it.dailyWaterIntake
    } ?: false

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                onUpdateWaterTrack = onUpdateWaterTrack,
                waterTracksLoadingController = viewModel.waterTracksLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
                statisticLoadingController = viewModel.statisticLoadingController,
                millilitersDrunkLoadingController = viewModel.millilitersDrunkLoadingController
            )
        },
        onFabClick = if (showFab) onSaveWaterTrack else null,
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

@Composable
private fun WaterTrackingContent(
    onUpdateWaterTrack: (WaterTrackingItem) -> Unit,
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    statisticLoadingController: LoadingController<WaterTrackingStatistic?>,
    millilitersDrunkLoadingController: LoadingController<MillilitersDrunkToDailyRate>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LoadingContainer(
            controller1 = waterTracksLoadingController,
            controller2 = chartEntriesLoadingController,
            controller3 = statisticLoadingController,
            controller4 = millilitersDrunkLoadingController,
        ) { waterTracks, chartEntries, statistic, millilitersDrunk ->
            DailyRateSection(millilitersDrunk)
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
                    onUpdateWaterTrack = onUpdateWaterTrack
                )
            } else {
                EmptySection(emptyTitleResId = R.string.waterTracking_nowEmpty_text)
            }
        }
    }
}

@Composable
private fun DailyRateSection(millilitersDrunk: MillilitersDrunkToDailyRate) {
    Headline(
        text = stringResource(
            id = R.string.waterTracking_millilitersCount_formatText,
            formatArgs = arrayOf(
                millilitersDrunk.millilitersDrunkCount,
                millilitersDrunk.dailyWaterIntake
            )
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    LinearProgressBar(progress = millilitersDrunk.millilitersDrunkCount safeDiv millilitersDrunk.dailyWaterIntake)
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
            }
        )
    } else {
        Description(text = stringResource(id = R.string.pedometer_chartNotEnoughData_text))
    }
}

@Composable
private fun WaterTrackingStatisticSection(waterTrackingStatistic: WaterTrackingStatistic) {
    val uiModule = LocalUIModule.current
    val waterTrackingStatisticResolver = uiModule.waterTrackingStatisticResolver

    Title(text = stringResource(id = R.string.waterTracking_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = waterTrackingStatisticResolver.resolve(statistic = waterTrackingStatistic))
}

@Composable
private fun ColumnScope.TrackDiarySection(
    waterTracks: List<WaterTrackingItem>,
    onUpdateWaterTrack: (WaterTrackingItem) -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_diary_text))
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(waterTracks) { track ->
            WaterTrackItem(
                waterTrackingItem = track,
                onUpdate = onUpdateWaterTrack
            )
        }
    }
}
