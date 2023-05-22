package hardcoder.dev.androidApp.ui.features.diary.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrack
import hardcoder.dev.uikit.text.Description

@Composable
fun DiaryMoodItem(
    moodTrack: MoodTrack
) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

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
}