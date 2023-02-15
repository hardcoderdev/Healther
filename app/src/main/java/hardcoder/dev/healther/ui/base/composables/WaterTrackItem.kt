@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.base.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.DrinkType
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.DrinkTypeImageResolver
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackItem

@Composable
fun WaterTrackItem(
    waterTrackItem: WaterTrackItem,
    onDelete: (Int) -> Unit,
    onUpdate: (WaterTrackItem) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp, pressedElevation = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onUpdate(waterTrackItem) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = waterTrackItem.imageResId),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Text(
                    text = stringResource(id = waterTrackItem.drinkNameResId),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.waterTrackItem_formatMilliliters_text,
                        waterTrackItem.millilitersCount
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(
                modifier = Modifier.weight(0.5f),
                onClick = { onDelete(waterTrackItem.id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.waterTrackItem_deleteTrack_iconContentDescription)
                )
            }
        }
    }
}

@Preview
@Composable
fun WaterTrackItemPreview() {
    WaterTrackItem(
        waterTrackItem = WaterTrackItem(
            id = 1,
            imageResId = R.drawable.beer,
            drinkNameResId = R.string.waterTrackItem_beerDrinkType_text,
            timeInMillis = System.currentTimeMillis(),
            millilitersCount = 200,
            resolvedMillilitersCount = 200
        ),
        onDelete = {},
        onUpdate = {}
    )
}