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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.datetime.createRangeForThisDay
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.calendar.CustomMonthHeader
import hardcoder.dev.uikit.card.CardInfoItem
import hardcoder.dev.uikit.card.StaticCard
import hardcoder.dev.uikit.charts.ActivityLineChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.math.roundToInt

@Composable
fun PedometerHistory(onGoBack: () -> Unit) {
    val viewModel = koinViewModel<PedometerHistoryViewModel>()

    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                statisticLoadingController = viewModel.statisticLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometer_history_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun PedometerHistoryContent(
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    LaunchedEffect(key1 = calendarState.selectionState.selection) {
        val date = calendarState.selectionState.selection
            .firstOrNull()?.toKotlinLocalDate() ?: LocalDate.now()
        dateRangeInputController.changeInput(date.createRangeForThisDay())
    }

    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController
    ) { statistic, chartEntries ->
        Column(Modifier.padding(16.dp)) {
            SelectableCalendar(
                calendarState = calendarState,
                monthHeader = { monthState ->
                    CustomMonthHeader(monthState = monthState)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PedometerTracksHistory(statistic)
            Spacer(modifier = Modifier.height(16.dp))
            if (chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
                ActivityLineChart(
                    modifier = Modifier.weight(2f),
                    chartEntries = chartEntries,
                    xAxisValueFormatter = { value, _ ->
                        value.roundToInt().toString()
                    },
                    yAxisValueFormatter = { value, _ ->
                        value.roundToInt().toString()
                    }
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.pedometer_history_weDontHaveEnoughDataToShowChart_text))
            }
        }
    }
}

@Composable
private fun PedometerTracksHistory(
    state: PedometerStatistic
) {
    val decimalFormatter = koinInject<DecimalFormatter>()

    Spacer(modifier = Modifier.height(16.dp))
    if (state.totalSteps != 0) {
        Title(text = stringResource(id = R.string.pedometer_history_yourIndicatorsForThisDay_text))
        Spacer(modifier = Modifier.height(16.dp))
        StaticCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardInfoItem(
                    iconResId = R.drawable.ic_directions_walk,
                    nameResId = R.string.pedometer_stepsLabel_text,
                    value = state.totalSteps.toString()
                )
                CardInfoItem(
                    iconResId = R.drawable.ic_my_location,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = decimalFormatter.roundAndFormatToString(state.totalKilometers)
                )
                CardInfoItem(
                    iconResId = R.drawable.ic_fire,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = decimalFormatter.roundAndFormatToString(state.totalCalories)
                )
            }
        }
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.pedometer_emptyDayHistory_text))
    }
}