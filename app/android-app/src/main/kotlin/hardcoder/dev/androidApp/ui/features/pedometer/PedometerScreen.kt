package hardcoder.dev.androidApp.ui.features.pedometer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.NotAvailable
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.card.CardInfoItem
import hardcoder.dev.uikit.card.StaticCard
import hardcoder.dev.uikit.charts.ActivityLineChart
import hardcoder.dev.uikit.charts.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.icons.IconButton
import hardcoder.dev.uikit.progressBar.LinearProgressBar
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.sections.Initial
import hardcoder.dev.uikit.sections.PermissionsSection
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoder.dev.uikit.text.Title
import kotlin.math.roundToInt

@Composable
fun PedometerScreen(
    onGoBack: () -> Unit,
    onGoToPedometerHistory: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getPedometerViewModel() }

    ScaffoldWrapper(
        content = {
            PedometerContent(
                viewModel.chartEntriesLoadingController,
                viewModel.dailyRateStepsLoadingController,
                viewModel.pedometerAvailabilityLoadingController,
                viewModel.todayStatisticLoadingController,
                viewModel.statisticLoadingController,
                viewModel.pedometerToggleController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometer_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToPedometerHistory
                )
            )
        )
    )
}

@Composable
private fun PedometerContent(
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    pedometerAvailabilityLoadingController: LoadingController<PedometerManager.Availability>,
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    pedometerToggleController: ToggleController
) {
    val uiModule = LocalUIModule.current
    val pedometerRejectedMapper = uiModule.pedometerRejectedMapper

    LoadingContainer(pedometerAvailabilityLoadingController) { availability ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (availability) {
                is Available -> AvailablePedometerSection(
                    chartEntriesLoadingController = chartEntriesLoadingController,
                    dailyRateStepsLoadingController = dailyRateStepsLoadingController,
                    todayStatisticLoadingController = todayStatisticLoadingController,
                    statisticLoadingController = statisticLoadingController,
                    pedometerToggleController = pedometerToggleController
                )

                is NotAvailable -> {
                    PermissionsSection(
                        animationRepeatTimes = 1,
                        initial = Initial(
                            animationResId = R.raw.permissions,
                            titleResId = R.string.pedometer_permissions_not_granted_title_text,
                        ),
                        onGrant = pedometerToggleController::toggle,
                        rejected = pedometerRejectedMapper.mapReasonToRejected(availability.rejectReason)
                    )
                }
            }
        }
    }
}

@Composable
private fun AvailablePedometerSection(
    chartEntriesLoadingController: LoadingController<List<Pair<Int, Int>>>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    pedometerToggleController: ToggleController
) {
    DailyRateSection(
        todayStatisticLoadingController = todayStatisticLoadingController,
        dailyRateStepsLoadingController = dailyRateStepsLoadingController,
        pedometerToggleController = pedometerToggleController
    )

    Spacer(modifier = Modifier.height(32.dp))

    LoadingContainer(
        controller1 = chartEntriesLoadingController,
        controller2 = todayStatisticLoadingController,
        controller3 = statisticLoadingController
    ) { chartEntries, todayStatistic, statistic ->
        if (todayStatistic.totalSteps > 0) {
            PedometerInfoSection(todayStatistic)
            Spacer(modifier = Modifier.height(32.dp))
            PedometerStatisticSection(statistic)
            Spacer(modifier = Modifier.height(32.dp))
            PedometerActivityChart(chartEntries)
        } else {
            EmptySection(emptyTitleResId = R.string.pedometer_nowEmpty_text)
        }
    }
}

@Composable
private fun DailyRateSection(
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    pedometerToggleController: ToggleController
) {
    LoadingContainer(
        controller1 = todayStatisticLoadingController,
        controller2 = dailyRateStepsLoadingController
    ) { todayStatistic, dailyRateSteps ->
        val pedometerTrackingState by pedometerToggleController.state.collectAsState()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Headline(
                text = stringResource(
                    id = R.string.pedometer_stepCountFormat_text,
                    todayStatistic.totalSteps ?: 0,
                    dailyRateSteps
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = todayStatistic.totalSteps <= dailyRateSteps) {
                IconButton(
                    onClick = pedometerToggleController::toggle,
                    iconResId = if (pedometerTrackingState.isActive) R.drawable.ic_stop else R.drawable.ic_play,
                    contentDescription = if (pedometerTrackingState.isActive) {
                        R.string.pedometer_stopIcon_contentDescription
                    } else {
                        R.string.pedometer_playIcon_contentDescription
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressBar(progress = todayStatistic.totalSteps safeDiv dailyRateSteps)
    }

}

@Composable
private fun PedometerInfoSection(statistic: PedometerStatistic) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val decimalFormatter = uiModule.decimalFormatter

    Title(text = stringResource(id = R.string.pedometer_yourIndicatorsForThisDay_text))
    Spacer(modifier = Modifier.height(16.dp))
    StaticCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardInfoItem(
                iconResId = R.drawable.ic_my_location,
                nameResId = R.string.pedometer_kilometersLabel_text,
                value = decimalFormatter.roundAndFormatToString(statistic.totalKilometers)
            )
            CardInfoItem(
                iconResId = R.drawable.ic_fire,
                nameResId = R.string.pedometer_caloriesLabel_text,
                value = decimalFormatter.roundAndFormatToString(statistic.totalCalories)
            )
            CardInfoItem(
                iconResId = R.drawable.ic_time,
                nameResId = R.string.pedometer_timeLabel_text,
                value = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = statistic.totalDuration.inWholeMilliseconds,
                    accuracy = DateTimeFormatter.Accuracy.MINUTES
                )
            )
        }
    }
}

@Composable
private fun PedometerStatisticSection(statistic: PedometerStatistic) {
    val uiModule = LocalUIModule.current
    val pedometerStatisticResolver = uiModule.pedometerStatisticResolver

    Title(text = stringResource(id = R.string.pedometer_statistic_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = pedometerStatisticResolver.resolve(statistic))
}

@Composable
private fun PedometerActivityChart(chartEntries: List<Pair<Int, Int>>) {
    Title(text = stringResource(id = R.string.waterTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(400.dp),
            chartEntries = chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            }
        )
    } else {
        Description(text = stringResource(id = R.string.pedometer_chartNotEnoughData_text))
    }
}

