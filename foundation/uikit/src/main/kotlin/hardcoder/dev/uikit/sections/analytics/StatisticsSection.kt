package hardcoder.dev.uikit.sections.analytics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.statistic.StatisticData
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.app.resources.R

@Composable
fun StatisticsSection(
    statisticsDataList: List<StatisticData>,
) {
    Title(text = stringResource(id = R.string.analytics_generalStatistics_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = statisticsDataList)
}

@Preview
@Composable
private fun StatisticsSectionPreview() {
    HealtherThemePreview {
        StatisticsSection(
            statisticsDataList = listOf(
                StatisticData("Name1", "Value1"),
                StatisticData("Name2", "Value2"),
                StatisticData("Name3", "Value3"),
                StatisticData("Name4", "Value4"),
            ),
        )
    }
}