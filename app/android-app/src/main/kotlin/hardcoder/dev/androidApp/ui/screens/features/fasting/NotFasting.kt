package hardcoder.dev.androidApp.ui.screens.features.fasting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticResolver
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistics.FastingStatisticSection
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.mock.dataProviders.features.FastingMockDataProvider
import hardcoder.dev.presentation.features.fasting.FastingChartData
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.chart.ActivityColumnChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlin.math.roundToInt

@Composable
fun NotFasting(
    fastingStatisticResolver: FastingStatisticResolver,
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    millisDistanceFormatter: MillisDistanceFormatter,
    chartData: FastingChartData,
    fastingStatistic: FastingStatistic?,
    lastFastingTracks: List<FastingTrack>,
    onCreateFastingTrack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            FastingLastTracksSection(
                millisDistanceFormatter = millisDistanceFormatter,
                dateTimeFormatter = dateTimeFormatter,
                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                lastFastingTrackList = lastFastingTracks,
            )
            Spacer(modifier = Modifier.height(32.dp))
            fastingStatistic?.let { statistic ->
                FastingStatisticSection(
                    fastingStatisticResolver = fastingStatisticResolver,
                    fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                    statistic = statistic,
                )
                Spacer(modifier = Modifier.height(32.dp))
                FastingChartSection(fastingChartData = chartData)
            } ?: run {
                EmptySection(emptyTitleResId = R.string.fasting_nowEmpty_text)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextIconButton(
            textIconButtonConfig = TextIconButtonConfig.Filled(
                iconResId = R.drawable.ic_play,
                labelResId = R.string.fasting_start_buttonText,
                onClick = onCreateFastingTrack,
            ),
        )
    }
}

@Composable
private fun FastingLastTracksSection(
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    dateTimeFormatter: DateTimeFormatter,
    millisDistanceFormatter: MillisDistanceFormatter,
    lastFastingTrackList: List<FastingTrack>,
) {
    Title(text = stringResource(id = R.string.fasting_lastFastingTracks_text))
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (lastFastingTrackList.isEmpty()) {
            Description(text = stringResource(id = R.string.fasting_lastTracksNotEnoughData_text))
        } else {
            lastFastingTrackList.forEach {
                FastingItem(
                    dateTimeFormatter = dateTimeFormatter,
                    millisDistanceFormatter = millisDistanceFormatter,
                    fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                    fastingTrack = it,
                )
            }
        }
    }
}

@Composable
private fun FastingChartSection(fastingChartData: FastingChartData) {
    Title(text = stringResource(id = R.string.fasting_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (fastingChartData.entriesList.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(200.dp),
            chartEntries = fastingChartData.entriesList.map { it.from to it.to },
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
        )
    } else {
        Description(text = stringResource(id = R.string.fasting_chartNotEnoughData_text))
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun NotFastingPreview() {
    HealtherTheme {
        ScaffoldWrapper(
            topBarConfig = TopBarConfig(
                type = TopBarType.TitleTopBar(
                    titleResId = R.string.fasting_title_topBar,
                ),
            ),
            content = {
                NotFasting(
                    onCreateFastingTrack = {},
                    fastingStatisticResolver = FastingStatisticResolver(context = LocalContext.current),
                    dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
                    fastingPlanResourcesProvider = FastingPlanResourcesProvider(),
                    millisDistanceFormatter = MillisDistanceFormatter(
                        context = LocalContext.current,
                        defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
                    ),
                    fastingStatistic = FastingMockDataProvider.fastingStatistics(),
                    chartData = FastingMockDataProvider.fastingChartData(),
                    lastFastingTracks = FastingMockDataProvider.fastingTracksList(),
                )
            },
        )
    }
}