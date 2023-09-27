package hardcoder.dev.uikit.components.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.text.Text
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun Statistics(
    modifier: Modifier = Modifier,
    statistics: List<StatisticData>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        statistics.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f),
                    text = item.name,
                    horizontalArrangement = Arrangement.Start,
                )
                Text(text = item.value)
            }

            if (item != statistics.last()) {
                Divider()
            }
        }
    }
}

@HealtherUiKitPreview
@Composable
private fun StatisticsPreview() {
    HealtherThemePreview {
        Statistics(
            statistics = listOf(
                StatisticData(
                    name = stringResource(id = R.string.placeholder_label),
                    value = "20",
                ),
                StatisticData(
                    name = stringResource(id = R.string.default_nowEmpty_text),
                    value = "40",
                ),
                StatisticData(
                    name = stringResource(id = R.string.placeholder_label),
                    value = "60",
                ),
                StatisticData(
                    name = stringResource(id = R.string.default_nowEmpty_text),
                    value = "80",
                ),
            ),
        )
    }
}