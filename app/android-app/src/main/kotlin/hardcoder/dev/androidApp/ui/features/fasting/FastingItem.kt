package hardcoder.dev.androidApp.ui.features.fasting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.progressBar.CircularProgressBar
import hardcoder.dev.uikit.text.Label

@Composable
fun FastingItem(fastingTrack: FastingTrack) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val fastingPlanResourcesProvider = uiModule.fastingPlanResourcesProvider

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

    val fastingProgressInMillis = fastingTrack.interruptedTime?.let {
        it - fastingTrack.startTime
    } ?: run {
        fastingTrack.duration
    }

    Card<FastingTrack>(interactionType = InteractionType.STATIC) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CircularProgressBar(
                fontSize = 16.sp,
                radius = 35.dp,
                percentage = fastingProgressInMillis.inWholeMilliseconds safeDiv fastingTrack.duration.inWholeMilliseconds,
                innerText = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = fastingDurationInMillis.inWholeMilliseconds,
                    accuracy = DateTimeFormatter.Accuracy.MINUTES
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.fillMaxWidth()) {
                Label(
                    text = stringResource(
                        id = R.string.fasting_item_fasting_startTime_formatText,
                        formatArgs = arrayOf(fastingStartDateAndTime)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Label(
                    text = stringResource(
                        id = R.string.fasting_item_fasting_endTime_formatText,
                        formatArgs = arrayOf(fastingEndDateAndTime)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Label(
                    text = stringResource(
                        id = R.string.fasting_item_fastingPlan_formatText,
                        formatArgs = arrayOf(
                            stringResource(
                                id = fastingPlanResourcesProvider.provide(
                                    fastingTrack.fastingPlan
                                ).nameResId
                            )
                        )
                    )
                )
            }
        }
    }
}