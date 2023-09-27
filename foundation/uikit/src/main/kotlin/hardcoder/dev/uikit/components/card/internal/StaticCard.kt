package hardcoder.dev.uikit.components.card.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material3.Card as MaterialCard

@Composable
internal fun StaticCard(
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit,
) {
    MaterialCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        cardContent()
    }
}

@HealtherUiKitPreview
@Composable
internal fun StaticCardPreview() {
    HealtherThemePreview {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            StaticCard(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp),
                cardContent = {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Title(
                            text = stringResource(id = R.string.placeholder_label),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            imageResId = R.drawable.uikit_sample_image,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                    }
                },
            )
        }
    }
}