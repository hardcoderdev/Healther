@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package hardcoder.dev.android_app.ui.features.pedometer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.pedometer.PedometerTrackItem

data class InfoItem(
    val icon: ImageVector,
    @StringRes val nameResId: Int,
    val value: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedometerInfoSection(infoItemList: List<InfoItem>) {
    Text(
        text = stringResource(id = R.string.pedometer_yourIndicatorsForThisDay_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        onClick = {},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            infoItemList.forEach {
                ItemInfo(
                    imageVector = it.icon,
                    valueLabelResId = it.nameResId,
                    value = it.value
                )
            }
        }
    }
}

@Composable
private fun ItemInfo(
    imageVector: ImageVector,
    @StringRes valueLabelResId: Int,
    value: String
) {
    Column {
        Icon(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = stringResource(id = valueLabelResId),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Preview
@Composable
fun PedometerTrackItemPreview() {
    val pedometerTrackItem = PedometerTrackItem(
        stepsCount = 1000,
        kilometersCount = 300.0f,
        caloriesBurnt = 29.3f,
        timeInMillis = 123456789
    )
    PedometerInfoSection(
        infoItemList = listOf(
            InfoItem(
                icon = Icons.Filled.LockClock,
                nameResId = R.string.pedometer_stepsLabel_text,
                value = pedometerTrackItem.stepsCount.toString()
            ),
            InfoItem(
                icon = Icons.Filled.MyLocation,
                nameResId = R.string.pedometer_kilometersLabel_text,
                value = pedometerTrackItem.kilometersCount.toString()
            ),
            InfoItem(
                icon = Icons.Filled.Fireplace,
                nameResId = R.string.pedometer_caloriesLabel_text,
                value = pedometerTrackItem.caloriesBurnt.toString()
            )
        )
    )
}