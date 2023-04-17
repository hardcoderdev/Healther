package hardcoder.dev.androidApp.ui.dashboard.diary.items

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.di.UIModule
import hardcoder.dev.androidApp.ui.icons.LocalIconImpl
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Label
import kotlinx.datetime.Clock

@Composable
fun DiaryMoodItem(
    moodTrack: MoodTrack,
    note: String,
    onUpdate: (MoodTrack) -> Unit
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

    Card(
        interactionType = InteractionType.ACTION,
        onClick = { onUpdate(moodTrack) },
        item = moodTrack
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = moodTrack.moodType.icon.resourceId),
                    contentDescription = moodTrack.moodType.name
                )
                Spacer(modifier = Modifier.width(16.dp))
                Description(
                    text = stringResource(
                        id = R.string.moodTracking_item_feel_formatText,
                        formatArgs = arrayOf(
                            moodTrack.moodType.name.lowercase(),
                            dateTimeFormatter.formatTime(moodTrack.date)
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = stringResource(R.string.diary_itemMood_yourNote_text))
            Spacer(modifier = Modifier.height(8.dp))
            Label(text = note)
        }
    }
}

@Preview
@Composable
fun DiaryMoodItemPreview() {
    DiaryMoodItem(
        moodTrack = MoodTrack(
            id = 0,
            moodType = MoodType(
                id = 0,
                name = "Happy",
                icon = LocalIconImpl(0, R.drawable.ic_baseball),
                positivePercentage = 70
            ),
            date = Clock.System.now()
        ),
        note = "Some note, description of the track",
        onUpdate = {}
    )
}