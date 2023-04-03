package hardcoder.dev.androidApp.ui.features.waterTracking.waterTrack

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
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.entities.features.waterTracking.DrinkType
import hardcoder.dev.presentation.features.waterTracking.WaterTrackItem
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title

@Composable
fun WaterTrackItem(
    waterTrackItem: WaterTrackItem,
    onUpdate: (WaterTrackItem) -> Unit
) {
    Card<DrinkType>(
        interactionType = InteractionType.ACTION,
        onClick = { onUpdate(waterTrackItem) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = waterTrackItem.drinkType.icon.resourceId),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Title(text = waterTrackItem.drinkType.name)
                Spacer(modifier = Modifier.height(8.dp))
                Description(
                    text = stringResource(
                        id = R.string.waterTracking_Item_milliliters_formatText,
                        formatArgs = arrayOf(
                            waterTrackItem.millilitersCount,
                            waterTrackItem.resolvedMillilitersCount
                        )
                    )
                )
            }
        }
    }
}