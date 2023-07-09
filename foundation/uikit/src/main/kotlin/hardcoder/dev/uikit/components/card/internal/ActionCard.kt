package hardcoder.dev.uikit.components.card.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.UiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ActionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    cardContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
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

@UiKitPhonePreview
@Composable
internal fun ActionCardPreview() {
    HealtherThemePreview {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ActionCard(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp),
                onClick = { /* Some selection logic here */ },
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