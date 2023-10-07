package hardcoder.dev.screens.features.moodTracking.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.moodTracking.MoodTrackingChartData
import hardcoder.dev.entities.features.moodTracking.MoodTrackingStatistics
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.resolvers.features.moodTracking.MoodTrackingStatisticResolver
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.analytics.ChartSection
import hardcoder.dev.uikit.sections.analytics.StatisticsSection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodTrackingAnalytics(
    moodTrackingStatisticResolver: MoodTrackingStatisticResolver,
    statisticLoadingController: LoadingController<MoodTrackingStatistics?>,
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
    statisticLoadingController: LoadingController<MoodTrackingStatistics?>,
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
                StatisticsSection(statisticsDataList = moodTrackingStatisticResolver.resolve(statistic))
                Spacer(modifier = Modifier.height(32.dp))
                ChartSection(
                    titleResId = R.string.moodTracking_activity_chart,
                    chartData = chartData.entriesList.map { it.from to it.to },
                )
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                EmptySection(emptyTitleResId = R.string.analytics_nowEmpty_text)
            }
        }
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingAnalyticsPreview() {
    HealtherTheme {
        MoodTrackingAnalytics(
            onGoBack = {},
            moodTrackingStatisticResolver = MoodTrackingStatisticResolver(
                context = LocalContext.current,
            ),
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