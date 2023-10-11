package hardcoder.dev.screens.features.foodTracking.foodTracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.entities.features.foodTracking.FoodTrack
import hardcoder.dev.mock.dataProviders.features.FoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme

@Composable
fun FoodTrackItem(
    modifier: Modifier = Modifier,
    onUpdateFoodTrack: (FoodTrack) -> Unit,
    foodTrack: FoodTrack,
) {
    Card(
        cardConfig = CardConfig.Action(
            onClick = { onUpdateFoodTrack(foodTrack) },
            cardContent = {
                Row(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    Column {
                        Title(text = foodTrack.foodType.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(text = foodTrack.calories.toString())
                    }
                }
            },
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTrackItemPreview() {
    HealtherTheme {
        FoodTrackItem(
            onUpdateFoodTrack = {},
            foodTrack = FoodTrackingMockDataProvider.foodTracksList()[0],
        )
    }
}