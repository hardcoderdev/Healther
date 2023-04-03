package hardcoder.dev.androidApp.ui.features.pedometer.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.roundAndFormatToString
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.calendar.CustomMonthHeader
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.card.CardInfo
import hardcoder.dev.uikit.card.CardInfoItem
import hardcoder.dev.uikit.charts.ActivityLineChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlin.math.roundToInt

@Composable
fun PedometerHistoryScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getPedometerHistoryViewModel()
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
                titleResId = R.string.pedometer_History_title_topBar,
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
        SelectableCalendar(
            calendarState = calendarState,
            monthHeader = { monthState ->
                CustomMonthHeader(monthState = monthState)
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PedometerTracksHistory(state = state)
        Spacer(modifier = Modifier.height(16.dp))
        if (state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
            ActivityLineChart(
                modifier = Modifier.weight(2f),
                chartEntries = state.chartEntries,
                xAxisValueFormatter = { value, _ ->
                    value.roundToInt().toString()
                },
                yAxisValueFormatter = { value, _ ->
                    value.roundToInt().toString()
                }
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Description(text = stringResource(id = R.string.pedometer_History_weDontHaveEnoughDataToShowChart_text))
        }
    }
}

@Composable
private fun PedometerTracksHistory(
    state: PedometerHistoryViewModel.State
) {
    Spacer(modifier = Modifier.height(16.dp))
    if (state.totalStepsCount != 0) {
        Title(text = stringResource(id = R.string.pedometer_History_yourIndicatorsForThisDay_text))
        Spacer(modifier = Modifier.height(16.dp))
        Card<CardInfo>(interactionType = InteractionType.STATIC) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardInfoItem(
                    iconResId = R.drawable.ic_directions_walk,
                    nameResId = R.string.pedometer_stepsLabel_text,
                    value = state.totalStepsCount.toString()
                )
                CardInfoItem(
                    iconResId = R.drawable.ic_my_location,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = state.totalKilometersCount.roundAndFormatToString()
                )
                CardInfoItem(
                    iconResId = R.drawable.ic_fire,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = state.totalCaloriesBurned.roundAndFormatToString()
                )
            }
        }
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.pedometer_emptyDayHistory_text))
    }
}