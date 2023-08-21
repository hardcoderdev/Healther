package hardcoder.dev.androidApp.ui.screens.features.moodTracking.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingAnalyticsViewModel
import hardcoder.dev.uikit.components.chart.ActivityColumnChart
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
fun MoodTrackingAnalytics(
    viewModel: MoodTrackingAnalyticsViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingAnalyticsContent(
                statisticLoadingController = viewModel.statisticLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_analytics_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTrackingAnalyticsContent(
    statisticLoadingController: LoadingController<MoodTrackingStatistic?>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>
) {
    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController,
    ) { statistic, chartEntries ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (chartEntries.isNotEmpty() && statistic != null) {
                StatisticSection(statistic = statistic)
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(chartEntries)
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.moodTracking_analyticsNowEmpty_text)
            }
        }
    }
}

@Composable
private fun StatisticSection(statistic: MoodTrackingStatistic) {
    val moodTrackingStatisticResolver = koinInject<MoodTrackingStatisticResolver>()

    Title(text = stringResource(id = R.string.moodTracking_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = moodTrackingStatisticResolver.resolve(statistic))
}

@Composable
private fun ChartSection(chartEntries: List<Pair<Int, Int>>) {
    Title(text = stringResource(id = R.string.moodTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(100.dp),
            chartEntries = chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                "${value.roundToInt()}%"
            },
        )
    } else {
        Description(text = stringResource(id = R.string.moodTracking_chartNotEnoughData_text))
    }
}