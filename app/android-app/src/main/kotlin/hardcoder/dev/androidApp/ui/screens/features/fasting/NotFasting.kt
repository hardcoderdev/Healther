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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.statistic.FastingStatisticSection
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.chart.ActivityColumnChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.app.android.app.R
import kotlin.math.roundToInt

@Composable
fun NotFasting(
    fastingChartEntries: List<Pair<Int, Long>>,
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
            FastingLastTracksSection(lastFastingTrackList = lastFastingTracks)
            Spacer(modifier = Modifier.height(32.dp))
            fastingStatistic?.let { statistic ->
                FastingStatisticSection(statistic = statistic)
                Spacer(modifier = Modifier.height(32.dp))
                FastingChartSection(fastingChartEntries = fastingChartEntries)
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
private fun FastingLastTracksSection(lastFastingTrackList: List<FastingTrack>) {
    Title(text = stringResource(id = R.string.fasting_lastFastingTracks_text))
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (lastFastingTrackList.isEmpty()) {
            Description(text = stringResource(id = R.string.fasting_lastTracksNotEnoughData_text))
        } else {
            lastFastingTrackList.forEach {
                FastingItem(fastingTrack = it)
            }
        }
    }
}

@Composable
private fun FastingChartSection(fastingChartEntries: List<Pair<Int, Long>>) {
    Title(text = stringResource(id = R.string.fasting_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (fastingChartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityColumnChart(
            isZoomEnabled = true,
            modifier = Modifier.height(200.dp),
            chartEntries = fastingChartEntries,
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