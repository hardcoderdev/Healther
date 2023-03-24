package hardcoder.dev.androidApp.ui.features.pedometer.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalFloatFormatter
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.pedometer.InfoItem
import hardcoder.dev.androidApp.ui.features.pedometer.PedometerInfoSection
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.uikit.ActivityChartSection
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.text.Title
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

const val MINIMUM_ENTRIES_FOR_SHOWING_CHART = 2

@Composable
fun PedometerHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createPedometerHistoryViewModel()
    }
    val state by viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                state = state,
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
        PedometerTracksHistory(state = state)
        Spacer(modifier = Modifier.height(16.dp))
        if (
            state.chartEntries.isNotEmpty() &&
            state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART
        ) {
            ActivityChartSection(
                modifier = Modifier.weight(2f),
                chartEntries = state.chartEntries
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Title(text = stringResource(id = R.string.pedometerHistory_weDontHaveEnoughDataToShowChart))
        }
    }
}

@Composable
private fun PedometerTracksHistory(
    state: PedometerHistoryViewModel.State
) {
    val floatFormatter = LocalFloatFormatter.current
    Spacer(modifier = Modifier.height(16.dp))

    if (state.totalStepsCount != 0) {
        PedometerInfoSection(
            infoItemList = listOf(
                InfoItem(
                    iconResId = R.drawable.ic_directions_walk,
                    nameResId = R.string.pedometer_stepsLabel_text,
                    value = state.totalStepsCount.toString()
                ),
                InfoItem(
                    iconResId = R.drawable.ic_my_location,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = floatFormatter.format(state.totalKilometersCount)
                ),
                InfoItem(
                    iconResId = R.drawable.ic_fire,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = floatFormatter.format(state.totalCaloriesBurned)
                )
            )
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Title(text = stringResource(id = R.string.pedometer_emptyDayHistory_text))
    }
}