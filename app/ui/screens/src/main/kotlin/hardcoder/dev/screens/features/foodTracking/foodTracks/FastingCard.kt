package hardcoder.dev.screens.features.foodTracking.foodTracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.card.Card
import hardcoder.dev.blocks.components.card.CardConfig
import hardcoder.dev.blocks.components.text.Description
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FastingCard(
    timeDifferenceSinceLastMeal: Long?,
    millisDistanceFormatter: MillisDistanceFormatter,
) {
    val descriptionText = timeDifferenceSinceLastMeal?.let {
        stringResource(
            R.string.foodTracking_fasting_format,
            millisDistanceFormatter.formatMillisDistance(
                accuracy = MillisDistanceFormatter.Accuracy.SECONDS,
                distanceInMillis = timeDifferenceSinceLastMeal,
                usePlurals = true,
            ),
        )
    } ?: stringResource(
        R.string.foodTracking_fasting_not_started,
    )

    Card(
        cardConfig = CardConfig.Static(
            cardContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Title(text = stringResource(R.string.foodTracking_fasting_title))
                    Spacer(modifier = Modifier.height(16.dp))
                    Description(text = descriptionText)
                }
            },
        ),
    )
}