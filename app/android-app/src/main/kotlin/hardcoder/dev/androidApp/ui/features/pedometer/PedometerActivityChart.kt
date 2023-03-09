package hardcoder.dev.androidApp.ui.features.pedometer

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf

const val MINIMUM_ENTRIES_FOR_SHOWING_CHART = 2

@Composable
fun PedometerActivityChart(
    modifier: Modifier = Modifier,
    chartEntries: List<Pair<Int, Int>>
) {
    val chartEntryModel = entryModelOf(entriesOf(*chartEntries.toTypedArray()))
    Chart(
        modifier = modifier.fillMaxSize(),
        chart = lineChart(
            lines = listOf(
                currentChartStyle.lineChart.lines[0].copy(
                    lineColor = MaterialTheme.colorScheme.primary.toArgb(),
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.inversePrimary
                            )
                        )
                    )
                )
            )
        ),
        model = chartEntryModel,
        startAxis = startAxis(
            titleComponent = textComponent(
                color = MaterialTheme.colorScheme.onBackground,
                background = shapeComponent(
                    Shapes.pillShape,
                    MaterialTheme.colorScheme.primary
                )
            )
        ),
        bottomAxis = bottomAxis(
            guideline = null,
            titleComponent = textComponent(
                color = MaterialTheme.colorScheme.onBackground,
                background = shapeComponent(
                    Shapes.pillShape,
                    MaterialTheme.colorScheme.primary
                ),
                typeface = Typeface.MONOSPACE,
            )
        )
    )
}