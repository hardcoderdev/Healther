package hardcoder.dev.uikit.components.card.internal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.material3.Card as MaterialCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectionCard(
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    isSelected: Boolean,
    cardContent: @Composable () -> Unit,
) {
    val selectedBorder = if (isSelected) {
        BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        BorderStroke(
            width = 0.dp,
            color = Color.Transparent,
        )
    }

    MaterialCard(
        onClick = onSelect,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.border(
            border = selectedBorder,
            shape = RoundedCornerShape(16.dp),
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
        ),
    ) {
        cardContent()
    }
}

@HealtherUiKitPreview
@Composable
internal fun SelectionCardPreview() {
    var isSelected by remember {
        mutableStateOf(true)
    }

    HealtherThemePreview {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SelectionCard(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp),
                isSelected = isSelected,
                onSelect = {
                    isSelected = !isSelected
                    /* Some selection logic here */
                },
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