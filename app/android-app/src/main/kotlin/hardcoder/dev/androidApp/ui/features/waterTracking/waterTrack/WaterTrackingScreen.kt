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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.presentation.features.waterTracking.WaterTrackItem
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
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
        DailyRateSection(state = state)
        Spacer(modifier = Modifier.height(32.dp))
        if (state.chartEntries.isNotEmpty() && state.statistic != null) {
            WaterTrackingStatisticSection(waterTrackingStatistic = requireNotNull(state.statistic))
            Spacer(modifier = Modifier.height(32.dp))
            WaterTrackingChartSection(state = state)
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (state.waterTracks.isNotEmpty()) {
            TrackDiarySection(state = state, onUpdateWaterTrack = onUpdateWaterTrack)
        } else {
            EmptySection(emptyTitleResId = R.string.waterTracking_nowEmpty_text)
        }
    }
}

@Composable
private fun DailyRateSection(state: WaterTrackingViewModel.State) {
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
}

@Composable
private fun WaterTrackingChartSection(state: WaterTrackingViewModel.State) {
    Title(text = stringResource(id = R.string.waterTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(100.dp),
            chartEntries = state.chartEntries,
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
    state: WaterTrackingViewModel.State,
    onUpdateWaterTrack: (WaterTrackItem) -> Unit
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
        items(state.waterTracks) { track ->
            WaterTrackItem(
                waterTrackItem = track,
                onUpdate = onUpdateWaterTrack
            )
        }
    }
}