package hardcoder.dev.androidApp.ui.features.starvation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.androidApp.ui.DateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.LocalTimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationTrack
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.CircularProgressBar
import hardcoder.dev.uikit.Text

@Composable
fun StarvationItem(starvationTrack: StarvationTrack) {
    val timeUnitMapper = LocalTimeUnitMapper.current

    val dateTimeFormatter = LocalDateTimeFormatter.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val starvationPlanResources =
        starvationPlanResourcesProvider.provide(starvationTrack.starvationPlan)

    val starvationEndDateAndTimeMillis = starvationTrack.interruptedTime?.let {
        starvationTrack.interruptedTime
    } ?: run {
        starvationTrack.startTime * starvationPlanResources.starvingHoursCount
    }

    val starvationStartDateAndTime = dateTimeFormatter.formatDateTime(starvationTrack.startTime)
    val starvationEndDateAndTime = dateTimeFormatter.formatDateTime(starvationEndDateAndTimeMillis)

    val starvationDurationInMillis = starvationTrack.interruptedTime?.let {
        it - starvationTrack.startTime
    } ?: run {
        timeUnitMapper.hoursToMillis(starvationPlanResources.starvingHoursCount.toLong())
    }

    val starvationProgressInMillis = starvationTrack.interruptedTime?.let {
        it - starvationTrack.startTime
    } ?: run {
        starvationDurationInMillis
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CircularProgressBar(
                color = MaterialTheme.colorScheme.primary,
                shadowColor = MaterialTheme.colorScheme.primaryContainer,
                fontSize = 16.sp,
                radius = 35.dp,
                percentage = starvationProgressInMillis safeDiv timeUnitMapper.hoursToMillis(
                    starvationPlanResources.starvingHoursCount.toLong()
                ),
                innerText = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = starvationDurationInMillis,
                    accuracy = DateTimeFormatter.Accuracy.MINUTES
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(
                        id = R.string.starvationItem_starvation_startTime_formatText,
                        formatArgs = arrayOf(starvationStartDateAndTime)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(
                        id = R.string.starvationItem_starvation_endTime_formatText,
                        formatArgs = arrayOf(starvationEndDateAndTime)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(
                        id = R.string.starvationItem_starvationPlan_text,
                        formatArgs = arrayOf(
                            stringResource(
                                id = starvationPlanResourcesProvider.provide(
                                    starvationTrack.starvationPlan
                                ).nameResId
                            )
                        )
                    )
                )
            }
        }
    }
}