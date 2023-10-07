package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.formatters.LiquidFormatter
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingChartData
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.analytics.ChartSection
import hardcoder.dev.uikit.sections.analytics.StatisticsSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

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
                titleResId = R.string.analytics_title_topBar,
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
                StatisticsSection(statisticsDataList = waterTrackingStatisticResolver.resolve(statistic = statistic))
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(
                    titleResId = R.string.waterTracking_analytics_activity_chart,
                    chartData = chartData.entriesList.map { it.from to it.to },
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.analytics_nowEmpty_text)
            }
        }
    }
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