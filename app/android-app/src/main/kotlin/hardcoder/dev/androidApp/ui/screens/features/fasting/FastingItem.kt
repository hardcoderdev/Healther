package hardcoder.dev.androidApp.ui.screens.features.fasting

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
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.logic.features.fasting.track.FastingTrack
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.progressBar.CircularProgressBar
import hardcoder.dev.uikit.components.text.Label
import hardcoderdev.healther.app.resources.R

@Composable
fun FastingItem(
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    millisDistanceFormatter: MillisDistanceFormatter,
    fastingTrack: FastingTrack,
) {
    Card(
        cardConfig = CardConfig.Static(
            cardContent = {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    ProgressSection(
                        millisDistanceFormatter = millisDistanceFormatter,
                        fastingTrack = fastingTrack,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    DateInfoSection(
                        fastingTrack = fastingTrack,
                        dateTimeFormatter = dateTimeFormatter,
                        fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                    )
                }
            },
        ),
    )
}

@Composable
private fun ProgressSection(
    millisDistanceFormatter: MillisDistanceFormatter,
    fastingTrack: FastingTrack,
) {
    val fastingTimePassedInMillis = fastingTrack.interruptedTime?.let {
        it - fastingTrack.startTime
    } ?: run {
        fastingTrack.duration
    }

    CircularProgressBar(
        fontSize = 16.sp,
        radius = 35.dp,
        percentage = fastingTrack.fastingProgress,
        innerText = millisDistanceFormatter.formatMillisDistance(
            distanceInMillis = fastingTimePassedInMillis.inWholeMilliseconds,
            accuracy = MillisDistanceFormatter.Accuracy.MINUTES,
        ),
    )
}

@Composable
private fun DateInfoSection(
    dateTimeFormatter: DateTimeFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    fastingTrack: FastingTrack,
) {
    val fastingEndDateAndTimeMillis = fastingTrack.interruptedTime ?: run {
        fastingTrack.startTime + fastingTrack.duration
    }

    val fastingStartDateAndTime = dateTimeFormatter.formatTime(fastingTrack.startTime)
    val fastingEndDateAndTime = dateTimeFormatter.formatTime(fastingEndDateAndTimeMillis)

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