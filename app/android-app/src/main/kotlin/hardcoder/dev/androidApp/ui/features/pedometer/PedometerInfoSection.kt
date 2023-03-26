package hardcoder.dev.androidApp.ui.features.pedometer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Label
import hardcoder.dev.uikit.text.Title

data class InfoItem(
    @DrawableRes val iconResId: Int,
    @StringRes val nameResId: Int,
    val value: String
)

@Composable
fun PedometerInfoSection(infoItemList: List<InfoItem>) {
    Title(text = stringResource(id = R.string.pedometer_yourIndicatorsForThisDay_text))
    Spacer(modifier = Modifier.height(16.dp))
    Card<InfoItem>(interactionType = InteractionType.STATIC) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            infoItemList.forEach {
                ItemInfo(
                    iconResId = it.iconResId,
                    valueLabelResId = it.nameResId,
                    value = it.value
                )
            }
        }
    }
}

@Composable
private fun ItemInfo(
    @DrawableRes iconResId: Int,
    @StringRes valueLabelResId: Int,
    value: String
) {
    Column {
        Icon(iconResId = iconResId, contentDescription = stringResource(id = valueLabelResId))
        Spacer(modifier = Modifier.height(8.dp))
        Title(text = value)
        Label(text = stringResource(id = valueLabelResId))
    }
}


@Preview
@Composable
fun PedometerTrackItemPreview() {
    val stepsCount = 1000
    val kilometersCount = 300.0f
    val caloriesBurnt = 29.3f

    PedometerInfoSection(
        infoItemList = listOf(
            InfoItem(
                iconResId = R.drawable.ic_time,
                nameResId = R.string.pedometer_stepsLabel_text,
                value = stepsCount.toString()
            ),
            InfoItem(
                iconResId = R.drawable.ic_my_location,
                nameResId = R.string.pedometer_kilometersLabel_text,
                value = kilometersCount.toString()
            ),
            InfoItem(
                iconResId = R.drawable.ic_fire,
                nameResId = R.string.pedometer_caloriesLabel_text,
                value = caloriesBurnt.toString()
            )
        )
    )
}