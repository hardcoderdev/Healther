package hardcoder.dev.uikit.components.chart

import android.graphics.Typeface

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shape.composeShape
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import hardcoder.dev.uikit.preview.UiKitPhonePreview

@Composable
fun ActivityColumnChart(
    isZoomEnabled: Boolean,
    modifier: Modifier = Modifier,
    spacing: Dp = 33.dp,
    chartEntries: List<Pair<Number, Number>>,
    xAxisValueFormatter: (Float, ChartValues) -> CharSequence,
    yAxisValueFormatter: (Float, ChartValues) -> CharSequence,
) {
    val chartEntryModel = entryModelOf(entriesOf(*chartEntries.toTypedArray()))
    Chart(
        isZoomEnabled = isZoomEnabled,
        modifier = modifier.fillMaxSize(),
        chart = columnChart(
            spacing = spacing,
            columns = listOf(
                lineComponent(
                    color = MaterialTheme.colorScheme.primary,
                    shape = Shapes.pillShape.composeShape(),
                    dynamicShader = DynamicShaders.fromBrush(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.inversePrimary,
                            ),
                        ),
                    ),
                ),
            ),
        ),
        model = chartEntryModel,
        startAxis = startAxis(
            titleComponent = textComponent(
                color = MaterialTheme.colorScheme.onBackground,
                background = shapeComponent(
                    Shapes.pillShape,
                    MaterialTheme.colorScheme.primary,
                ),
            ),
            valueFormatter = yAxisValueFormatter,
        ),
        bottomAxis = bottomAxis(
            guideline = null,
            titleComponent = textComponent(
                color = MaterialTheme.colorScheme.onBackground,
                background = shapeComponent(
                    Shapes.pillShape,
                    MaterialTheme.colorScheme.primary,
                ),
                typeface = Typeface.MONOSPACE,
            ),
            valueFormatter = xAxisValueFormatter,
        ),
    )
}

@UiKitPhonePreview
@Composable
private fun ActivityColumnChartPreview() {
    ActivityColumnChart(
        isZoomEnabled = true,
        chartEntries = listOf(
            1 to 2,
            2 to 3,
            3 to 4,
            4 to 5,
            5 to 6,
        ),
        xAxisValueFormatter = { value, _ -> value.toString() },
        yAxisValueFormatter = { value, _ -> value.toString() },
    )
}