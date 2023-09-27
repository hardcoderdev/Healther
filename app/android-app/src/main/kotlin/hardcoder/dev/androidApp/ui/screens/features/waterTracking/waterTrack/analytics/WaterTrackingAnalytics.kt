package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingChartData
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
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlin.math.roundToInt

@Composable
fun WaterTrackingAnalytics(
    waterTrackingStatisticResolver: WaterTrackingStatisticResolver,
    statisticLoadingController: LoadingController<WaterTrackingStatistic?>,
    chartEntriesLoadingController: LoadingController<WaterTrackingChartData>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingAnalyticsContent(
                waterTrackingStatisticResolver = waterTrackingStatisticResolver,
                chartEntriesLoadingController = chartEntriesLoadingController,
                statisticLoadingController = statisticLoadingController,
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
    waterTrackingStatisticResolver: WaterTrackingStatisticResolver,
    chartEntriesLoadingController: LoadingController<WaterTrackingChartData>,
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
        ) { chartData, statistic ->
            if (chartData.entriesList.isNotEmpty() && statistic != null) {
                StatisticSection(
                    waterTrackingStatisticResolver = waterTrackingStatisticResolver,
                    waterTrackingStatistic = statistic,
                )
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(chartData = chartData)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.waterTracking_analyticsNowEmpty_text)
            }
        }
    }
}

@Composable
private fun ChartSection(chartData: WaterTrackingChartData) {
    Title(text = stringResource(id = R.string.waterTracking_analytics_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartData.entriesList.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(100.dp),
            chartEntries = chartData.entriesList.map { it.from to it.to },
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
private fun StatisticSection(
    waterTrackingStatisticResolver: WaterTrackingStatisticResolver,
    waterTrackingStatistic: WaterTrackingStatistic,
) {
    Title(text = stringResource(id = R.string.waterTracking_analytics_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = waterTrackingStatisticResolver.resolve(statistic = waterTrackingStatistic))
}

@HealtherScreenPhonePreviews
@Composable
private fun WaterTrackingAnalyticsPreview() {
    HealtherTheme {
        WaterTrackingAnalytics(
            onGoBack = {},
            waterTrackingStatisticResolver = WaterTrackingStatisticResolver(
                context = LocalContext.current,
                liquidFormatter = LiquidFormatter(
                    context = LocalContext.current,
                    defaultAccuracy = LiquidFormatter.Accuracy.MILLILITERS,
                ),
            ),
            statisticLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.waterTrackingStatistics(
                    context = LocalContext.current,
                ),
            ),
            chartEntriesLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.waterTrackingChartData(),
            ),
        )
    }
}