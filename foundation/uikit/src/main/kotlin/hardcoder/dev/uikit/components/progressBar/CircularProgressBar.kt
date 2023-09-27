package hardcoder.dev.uikit.components.progressBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material3.Text as MaterialText

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    innerText: String,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    shadowColor: Color? = MaterialTheme.colorScheme.primaryContainer,
    strokeWidth: Dp = 8.dp,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        label = "currentPercentageOfCircularProgressBar",
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay,
        ),
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius.times(2)),
    ) {
        Canvas(modifier = Modifier.size(radius.times(2))) {
            shadowColor?.let {
                drawArc(
                    color = it,
                    startAngle = -90f,
                    sweepAngle = 360f * 100,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )
            }
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * currentPercentage,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                ),
            )
        }

        MaterialText(
            text = innerText,
            modifier = Modifier.width(radius.times(2) - 20.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@HealtherUiKitPreview
@Composable
private fun CircularProgressBarPreview() {
    HealtherThemePreview {
        CircularProgressBar(
            percentage = 0.7f,
            innerText = stringResource(id = R.string.placeholder_label),
            fontSize = 16.sp,
            radius = 80.dp,
            color = MaterialTheme.colorScheme.primary,
            shadowColor = MaterialTheme.colorScheme.primaryContainer,
            strokeWidth = 8.dp,
            modifier = Modifier,
            animationDuration = 1000,
            animationDelay = 0,
        )
    }
}