package hardcoder.dev.androidApp.ui.screens.features.diary.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.uikit.components.progressBar.CircularProgressBar
import hardcoder.dev.uikit.components.text.Label
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun DiaryFastingItem(fastingTrack: FastingTrack) {
    val dateTimeFormatter = koinInject<DateTimeFormatter>()
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
    val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()

    val fastingEndDateAndTimeMillis = fastingTrack.interruptedTime ?: run {
        fastingTrack.startTime + fastingTrack.duration
    }

    val fastingStartDateAndTime = dateTimeFormatter.formatTime(fastingTrack.startTime)
    val fastingEndDateAndTime =
        dateTimeFormatter.formatTime(fastingEndDateAndTimeMillis)

    val fastingDurationInMillis = fastingTrack.interruptedTime?.let {
        it - fastingTrack.startTime
    } ?: run {
        fastingTrack.duration
    }

    Row(Modifier.fillMaxWidth()) {
        CircularProgressBar(
            fontSize = 16.sp,
            radius = 35.dp,
            percentage = fastingTrack.fastingProgress,
            innerText = millisDistanceFormatter.formatMillisDistance(
                distanceInMillis = fastingDurationInMillis.inWholeMilliseconds,
                accuracy = MillisDistanceFormatter.Accuracy.MINUTES,
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(Modifier.fillMaxWidth()) {
            Label(
                text = stringResource(
                    id = R.string.fasting_itemFasting_startTime_formatText,
                    formatArgs = arrayOf(fastingStartDateAndTime),
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Label(
                text = stringResource(
                    id = R.string.fasting_itemFasting_endTime_formatText,
                    formatArgs = arrayOf(fastingEndDateAndTime),
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Label(
                text = stringResource(
                    id = R.string.fasting_itemFastingPlan_formatText,
                    formatArgs = arrayOf(
                        stringResource(
                            id = fastingPlanResourcesProvider.provide(
                                fastingTrack.fastingPlan,
                            ).nameResId,
                        ),
                    ),
                ),
            )
        }
    }
}