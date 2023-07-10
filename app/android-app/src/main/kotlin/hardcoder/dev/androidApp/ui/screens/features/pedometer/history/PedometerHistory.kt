package hardcoder.dev.androidApp.ui.screens.features.pedometer.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.androidApp.ui.screens.features.pedometer.PedometerInfoCard
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.presentation.features.pedometer.PedometerHistoryViewModel
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import kotlin.math.roundToInt
import kotlinx.datetime.Instant
import org.koin.compose.koinInject

@Composable
fun PedometerHistory(
    viewModel: PedometerHistoryViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                dateRangeInputController = viewModel.dateRangeInputController,
                statisticLoadingController = viewModel.statisticLoadingController,
                chartEntriesLoadingController = viewModel.chartEntriesLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometer_history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun PedometerHistoryContent(
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
) {
    val dateTimeProvider = koinInject<DateTimeProvider>()

    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController,
    ) { statistic, chartEntries ->
        Column(Modifier.padding(16.dp)) {
            SingleSelectionCalendar(
                dateTimeProvider = dateTimeProvider,
                inputChanged = { date ->
                    dateRangeInputController.changeInput(
                        date.getStartOfDay()..date.getEndOfDay(),
                    )
                },
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
                    },
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
    state: PedometerStatistic,
) {
    val decimalFormatter = koinInject<DecimalFormatter>()

    Spacer(modifier = Modifier.height(16.dp))
    if (state.totalSteps != 0) {
        Title(text = stringResource(id = R.string.pedometer_history_yourIndicatorsForThisDay_text))
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            cardConfig = CardConfig.Static(
                cardContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        PedometerInfoCard(
                            iconResId = R.drawable.ic_directions_walk,
                            nameResId = R.string.pedometer_stepsLabel_text,
                            value = state.totalSteps.toString(),
                        )
                        PedometerInfoCard(
                            iconResId = R.drawable.ic_my_location,
                            nameResId = R.string.pedometer_kilometersLabel_text,
                            value = decimalFormatter.roundAndFormatToString(state.totalKilometers),
                        )
                        PedometerInfoCard(
                            iconResId = R.drawable.ic_fire,
                            nameResId = R.string.pedometer_caloriesLabel_text,
                            value = decimalFormatter.roundAndFormatToString(state.totalCalories),
                        )
                    }
                },
            ),
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.pedometer_emptyDayHistory_text))
    }
}