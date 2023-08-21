package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingAnalyticsViewModel
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlin.math.roundToInt
import org.koin.compose.koinInject

@Composable
fun WaterTrackingAnalytics(
    viewModel: WaterTrackingAnalyticsViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingAnalyticsContent(
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
                statisticLoadingController = viewModel.statisticLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_analytics_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun WaterTrackingAnalyticsContent(
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    statisticLoadingController: LoadingController<WaterTrackingStatistic?>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        LoadingContainer(
            controller1 = chartEntriesLoadingController,
            controller2 = statisticLoadingController,
        ) { chartEntries, statistic ->
            if (chartEntries.isNotEmpty() && statistic != null) {
                StatisticSection(statistic)
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(chartEntries)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.waterTracking_analyticsNowEmpty_text)
            }
        }
    }
}

@Composable
private fun ChartSection(chartEntries: List<Pair<Int, Int>>) {
    Title(text = stringResource(id = R.string.waterTracking_analytics_activity_chart))
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
        Description(text = stringResource(id = R.string.waterTracking_analytics_chartNotEnoughData_text))
    }
}

@Composable
private fun StatisticSection(waterTrackingStatistic: WaterTrackingStatistic) {
    val waterTrackingStatisticResolver = koinInject<WaterTrackingStatisticResolver>()

    Title(text = stringResource(id = R.string.waterTracking_analytics_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = waterTrackingStatisticResolver.resolve(statistic = waterTrackingStatistic))
}