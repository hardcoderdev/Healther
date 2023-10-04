package hardcoder.dev.androidApp.ui.screens.features.moodTracking.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingChartData
import hardcoder.dev.logic.features.moodTracking.statistic.MoodTrackingStatistic
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
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
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlin.math.roundToInt

@Composable
fun MoodTrackingAnalytics(
    moodTrackingStatisticResolver: MoodTrackingStatisticResolver,
    statisticLoadingController: LoadingController<MoodTrackingStatistic?>,
    chartEntriesLoadingController: LoadingController<MoodTrackingChartData>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingAnalyticsContent(
                moodTrackingStatisticResolver = moodTrackingStatisticResolver,
                statisticLoadingController = statisticLoadingController,
                chartEntriesLoadingController = chartEntriesLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.analytics_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun MoodTrackingAnalyticsContent(
    moodTrackingStatisticResolver: MoodTrackingStatisticResolver,
    statisticLoadingController: LoadingController<MoodTrackingStatistic?>,
    chartEntriesLoadingController: LoadingController<MoodTrackingChartData>,
) {
    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController,
    ) { statistic, chartData ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (chartData.entriesList.isNotEmpty() && statistic != null) {
                StatisticSection(
                    moodTrackingStatisticResolver = moodTrackingStatisticResolver,
                    statistic = statistic,
                )
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(moodTrackingChartData = chartData)
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.analytics_nowEmpty_text)
            }
        }
    }
}

@Composable
private fun StatisticSection(
    moodTrackingStatisticResolver: MoodTrackingStatisticResolver,
    statistic: MoodTrackingStatistic,
) {
    Title(text = stringResource(id = R.string.analytics_generalStatistics_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = moodTrackingStatisticResolver.resolve(statistic))
}

@Composable
private fun ChartSection(moodTrackingChartData: MoodTrackingChartData) {
    Title(text = stringResource(id = R.string.moodTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (moodTrackingChartData.entriesList.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(100.dp),
            chartEntries = moodTrackingChartData.entriesList.map { it.from to it.to },
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                "${value.roundToInt()}%"
            },
        )
    } else {
        Description(text = stringResource(id = R.string.analytics_chartNotEnoughData_text))
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingAnalyticsPreview() {
    HealtherTheme {
        MoodTrackingAnalytics(
            onGoBack = {},
            moodTrackingStatisticResolver = MoodTrackingStatisticResolver(context = LocalContext.current),
            statisticLoadingController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodTrackingStatistics(
                    context = LocalContext.current,
                ),
            ),
            chartEntriesLoadingController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodTrackingChartData(),
            ),
        )
    }
}