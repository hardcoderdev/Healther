package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.icons.resourceId
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoderdev.healther.app.resources.R
import kotlinx.datetime.Clock

@Composable
fun WaterTrackItem(
    waterTrackingItem: WaterTrackingItem,
    onUpdate: (WaterTrackingItem) -> Unit,
) {
    Card(
        cardConfig = CardConfig.Action(
            onClick = { onUpdate(waterTrackingItem) },
            cardContent = {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(id = waterTrackingItem.drinkType.icon.resourceId),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                    ) {
                        Title(text = waterTrackingItem.drinkType.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(
                            text = stringResource(
                                id = R.string.waterTracking_Item_milliliters_formatText,
                                formatArgs = arrayOf(
                                    waterTrackingItem.millilitersCount,
                                    waterTrackingItem.resolvedMillilitersCount,
                                ),
                            ),
                        )
                    }
                }
            },
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
fun WaterTrackingItemPreview() {
    WaterTrackItem(
        onUpdate = {},
        waterTrackingItem = WaterTrackingItem(
            id = 0,
            millilitersCount = 1000,
            resolvedMillilitersCount = 800,
            isCollected = true,
            timeInMillis = Clock.System.now().toEpochMilliseconds(),
            drinkType = DrinkType(
                id = 0,
                name = stringResource(R.string.predefined_drinkType_name_coffee),
                icon = hardcoder.dev.icons.IconImpl(
                    id = 0,
                    resourceId = R.drawable.ic_history
                ),
                hydrationIndexPercentage = 80
            ),
        )
    )
}