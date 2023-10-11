package hardcoder.dev.screens.features.diary.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun DiaryMoodItem(
    dateTimeFormatter: DateTimeFormatter,
    moodTrack: MoodTrack,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = moodTrack.moodType.icon.resourceId),
            contentDescription = moodTrack.moodType.name,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Description(
            text = stringResource(
                id = R.string.moodTracking_item_feel_formatText,
                formatArgs = arrayOf(
                    moodTrack.moodType.name.lowercase(),
                    dateTimeFormatter.formatTime(moodTrack.date),
                ),
            ),
        )
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryMoodItemPreview() {
    HealtherTheme {
        DiaryMoodItem(
            dateTimeFormatter = DateTimeFormatter(context = LocalContext.current),
            moodTrack = MoodTrackingMockDataProvider.moodTracksList(
                context = LocalContext.current,
            ).first(),
        )
    }
}