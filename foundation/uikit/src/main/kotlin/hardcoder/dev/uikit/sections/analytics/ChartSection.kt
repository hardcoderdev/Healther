package hardcoder.dev.uikit.sections.analytics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.ui.resources.R
import kotlin.math.roundToInt

@Composable
fun ChartSection(
    @StringRes titleResId: Int,
    chartData: List<Pair<Number, Number>>,
) {
    Title(text = stringResource(id = titleResId))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartData.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(100.dp),
            chartEntries = chartData,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
        )
    } else {
        Description(text = stringResource(id = R.string.analytics_chartNotEnoughData_text))
    }
}

@Preview
@Composable
private fun ChartSectionPreview() {
    HealtherThemePreview {
        ChartSection(
            titleResId = R.string.moodTracking_activity_chart,
            chartData = listOf(
                0 to 1,
                2 to 3,
                4 to 5,
                6 to 7,
            ),
        )
    }
}