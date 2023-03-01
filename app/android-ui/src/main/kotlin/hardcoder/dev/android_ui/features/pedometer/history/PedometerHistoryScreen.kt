package hardcoder.dev.android_ui.features.pedometer.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.android_ui.LocalFloatFormatter
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.android_ui.features.pedometer.InfoItem
import hardcoder.dev.android_ui.features.pedometer.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.android_ui.features.pedometer.PedometerActivityChart
import hardcoder.dev.android_ui.features.pedometer.PedometerInfoSection
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.pedometer.PedometerHistoryViewModel
import hardcoder.dev.presentation.pedometer.PedometerTrackItem
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun PedometerHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createPedometerHistoryViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                state = state.value,
                onFetchPedometerTracks = {
                    viewModel.selectRange(it.createRangeForCurrentDay())
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.featureHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun PedometerHistoryContent(
    state: PedometerHistoryViewModel.State,
    onFetchPedometerTracks: (LocalDate) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        if (calendarState.selectionState.selection.isNotEmpty()) {
            onFetchPedometerTracks(
                calendarState.selectionState.selection.first().toKotlinLocalDate()
            )
        } else {
            onFetchPedometerTracks(LocalDate.now())
        }
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        PedometerTracksHistory(pedometerTrackItem = state.pedometerTrackItem)
        Spacer(modifier = Modifier.height(16.dp))
        if (
            state.chartEntries.isNotEmpty() &&
            state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART
        ) {
            PedometerActivityChart(
                modifier = Modifier.weight(2f),
                chartEntries = state.chartEntries
            )
        } else {
            Text(
                text = stringResource(id = R.string.pedometerHistory_weDontHaveEnoughDataToShowChart),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun PedometerTracksHistory(pedometerTrackItem: PedometerTrackItem?) {
    val floatFormatter = LocalFloatFormatter.current

    Spacer(modifier = Modifier.height(16.dp))

    pedometerTrackItem?.let {
        PedometerInfoSection(
            infoItemList = listOf(
                InfoItem(
                    icon = Icons.Filled.DirectionsWalk,
                    nameResId = R.string.pedometer_stepsLabel_text,
                    value = pedometerTrackItem.stepsCount.toString()
                ),
                InfoItem(
                    icon = Icons.Filled.MyLocation,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = floatFormatter.format(pedometerTrackItem.kilometersCount)
                ),
                InfoItem(
                    icon = Icons.Filled.Fireplace,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = floatFormatter.format(pedometerTrackItem.caloriesBurnt)
                )
            )
        )
    } ?: run {
        Text(
            text = stringResource(id = R.string.pedometer_emptyDayHistory_text),
            style = MaterialTheme.typography.titleMedium
        )
    }
}