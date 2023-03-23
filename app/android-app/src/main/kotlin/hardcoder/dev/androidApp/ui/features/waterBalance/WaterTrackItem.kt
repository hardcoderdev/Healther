@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.androidApp.ui.features.waterBalance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.LocalIconResolver
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.WaterTrackItem

@Composable
fun WaterTrackItem(
    waterTrackItem: WaterTrackItem,
    onUpdate: (WaterTrackItem) -> Unit
) {
    val iconResolver = LocalIconResolver.current
    val drinkType = waterTrackItem.drinkType

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
                painter = painterResource(id = iconResolver.toResourceId(drinkType.iconResourceName)),
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
                    text = drinkType.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.waterTrackItem_formatMilliliters_text,
                        waterTrackItem.millilitersCount,
                        waterTrackItem.resolvedMillilitersCount
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}