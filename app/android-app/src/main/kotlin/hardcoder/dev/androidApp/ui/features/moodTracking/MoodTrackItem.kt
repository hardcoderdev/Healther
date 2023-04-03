@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.moodTracking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.entities.features.moodTracking.Hobby
import hardcoder.dev.logic.entities.features.moodTracking.MoodTrack
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.text.Description

@Composable
fun MoodTrackItem(
    hobbyList: List<Hobby>,
    moodTrack: MoodTrack,
    onUpdate: (MoodTrack) -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

    val creationTimeInMillis = dateTimeFormatter.formatTime(time = moodTrack.date)

    Card(
        interactionType = InteractionType.ACTION,
        onClick = { onUpdate(moodTrack) },
        item = moodTrack
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = moodTrack.moodType.icon.resourceId),
                contentDescription = moodTrack.moodType.name
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Description(
                    text = stringResource(
                        id = R.string.moodTracking_Item_feel_formatText,
                        formatArgs = arrayOf(
                            moodTrack.moodType.name.lowercase(),
                            creationTimeInMillis
                        )
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    maxItemsInEachRow = 3
                ) {
                    hobbyList.forEach { hobbyTrack ->
                        Chip(
                            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                            text = hobbyTrack.name,
                            iconResId = moodTrack.moodType.icon.resourceId,
                            shape = RoundedCornerShape(16.dp),
                            padding = PaddingValues(8.dp),
                            isSelected = false,
                            interactionType = InteractionType.STATIC
                        )
                    }
                }
            }
        }
    }
}
