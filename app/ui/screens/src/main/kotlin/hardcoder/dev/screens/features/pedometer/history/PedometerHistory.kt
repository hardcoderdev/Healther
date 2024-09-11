package hardcoder.dev.screens.features.pedometer.history

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
import hardcoder.dev.blocks.components.card.Card
import hardcoder.dev.blocks.components.card.CardConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.text.Description
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.coroutines.DefaultBackgroundBackgroundCoroutineDispatchers
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.entities.features.pedometer.PedometerStatistics
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.mock.dataProviders.features.PedometerMockDataProvider
import hardcoder.dev.presentation.features.pedometer.PedometerChartData
import hardcoder.dev.screens.features.pedometer.PedometerInfoCard
import hardcoder.dev.uikit.components.calendar.SingleSelectionCalendar
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R
import kotlin.math.roundToInt
import kotlinx.datetime.Instant

@Composable
fun PedometerHistory(
    decimalFormatter: DecimalFormatter,
    dateTimeProvider: DateTimeProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    statisticLoadingController: LoadingController<PedometerStatistics>,
    chartEntriesLoadingController: LoadingController<PedometerChartData>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            PedometerHistoryContent(
                decimalFormatter = decimalFormatter,
                dateTimeProvider = dateTimeProvider,
                dateRangeInputController = dateRangeInputController,
                statisticLoadingController = statisticLoadingController,
                chartEntriesLoadingController = chartEntriesLoadingController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun PedometerHistoryContent(
    decimalFormatter: DecimalFormatter,
    dateTimeProvider: DateTimeProvider,
    dateRangeInputController: InputController<ClosedRange<Instant>>,
    statisticLoadingController: LoadingController<PedometerStatistics>,
    chartEntriesLoadingController: LoadingController<PedometerChartData>,
) {
    LoadingContainer(
        controller1 = statisticLoadingController,
        controller2 = chartEntriesLoadingController,
    ) { statistic, chartData ->
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
            PedometerTracksHistory(
                pedometerStatistics = statistic,
                decimalFormatter = decimalFormatter,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (chartData.entriesList.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
                ActivityLineChart(
                    modifier = Modifier.weight(2f),
                    chartEntries = chartData.entriesList.map { it.from to it.to },
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
    decimalFormatter: DecimalFormatter,
    pedometerStatistics: PedometerStatistics,
) {
    Spacer(modifier = Modifier.height(16.dp))
    if (pedometerStatistics.totalSteps != 0) {
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
                            value = pedometerStatistics.totalSteps.toString(),
                        )
                        PedometerInfoCard(
                            iconResId = R.drawable.ic_my_location,
                            nameResId = R.string.pedometer_kilometersLabel_text,
                            value = decimalFormatter.roundAndFormatToString(pedometerStatistics.totalKilometers),
                        )
                        PedometerInfoCard(
                            iconResId = R.drawable.ic_fire,
                            nameResId = R.string.pedometer_caloriesLabel_text,
                            value = decimalFormatter.roundAndFormatToString(pedometerStatistics.totalCalories),
                        )
                    }
                },
            ),
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.pedometer_history_emptyDayHistory_text))
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun PedometerHistoryPreview() {
    HealtherTheme {
        PedometerHistory(
            onGoBack = {},
            decimalFormatter = DecimalFormatter(),
            dateTimeProvider = DateTimeProvider(dispatchers = DefaultBackgroundBackgroundCoroutineDispatchers),
            dateRangeInputController = MockControllersProvider.inputController(MockDateProvider.instantRange()),
            chartEntriesLoadingController = MockControllersProvider.loadingController(
                data = PedometerMockDataProvider.pedometerChartData(),
            ),
            statisticLoadingController = MockControllersProvider.loadingController(
                data = PedometerMockDataProvider.pedometerStatistics(),
            ),
        )
    }
}