package hardcoder.dev.uikit.components.progressBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview

@Composable
fun LinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        progress = progress,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(16.dp),
    )
}

@UiKitPreview
@Composable
private fun LinearProgressBarPreview() {
    HealtherThemePreview {
        LinearProgressBar(
            progress = 0.7f,
            modifier = Modifier,
        )
    }
}