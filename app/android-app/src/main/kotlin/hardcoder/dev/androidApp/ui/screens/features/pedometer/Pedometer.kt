package hardcoder.dev.androidApp.ui.screens.features.pedometer

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.permissions.Initial
import hardcoder.dev.androidApp.ui.permissions.PermissionsSection
import hardcoder.dev.androidApp.ui.screens.features.pedometer.statistic.PedometerStatisticResolver
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.PedometerMockDataProvider
import hardcoder.dev.presentation.features.pedometer.PedometerChartData
import hardcoder.dev.presentation.features.pedometer.PedometerManager
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.chart.ActivityLineChart
import hardcoder.dev.uikit.components.chart.MINIMUM_ENTRIES_FOR_SHOWING_CHART
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Headline
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R
import kotlin.math.roundToInt

@Composable
fun Pedometer(
    pedometerStatisticResolver: PedometerStatisticResolver,
    millisDistanceFormatter: MillisDistanceFormatter,
    decimalFormatter: DecimalFormatter,
    pedometerRejectedMapper: PedometerRejectedMapper,
    chartEntriesLoadingController: LoadingController<PedometerChartData>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    dailyRateProgressController: LoadingController<Float>,
    pedometerAvailabilityLoadingController: LoadingController<PedometerManager.Availability>,
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    pedometerToggleController: ToggleController,
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
    onGoBack: () -> Unit,
    onGoToHistory: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            PedometerContent(
                pedometerStatisticResolver = pedometerStatisticResolver,
                millisDistanceFormatter = millisDistanceFormatter,
                decimalFormatter = decimalFormatter,
                pedometerRejectedMapper = pedometerRejectedMapper,
                chartEntriesLoadingController = chartEntriesLoadingController,
                dailyRateStepsLoadingController = dailyRateStepsLoadingController,
                pedometerAvailabilityLoadingController = pedometerAvailabilityLoadingController,
                todayStatisticLoadingController = todayStatisticLoadingController,
                statisticLoadingController = statisticLoadingController,
                pedometerToggleController = pedometerToggleController,
                dailyRateProgressController = dailyRateProgressController,
                rewardLoadingController = rewardLoadingController,
                collectRewardController = collectRewardController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometer_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToHistory,
                ),
            ),
        ),
    )
}

@Composable
private fun PedometerContent(
    pedometerStatisticResolver: PedometerStatisticResolver,
    millisDistanceFormatter: MillisDistanceFormatter,
    decimalFormatter: DecimalFormatter,
    pedometerRejectedMapper: PedometerRejectedMapper,
    chartEntriesLoadingController: LoadingController<PedometerChartData>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    dailyRateProgressController: LoadingController<Float>,
    pedometerAvailabilityLoadingController: LoadingController<PedometerManager.Availability>,
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    pedometerToggleController: ToggleController,
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
) {
    LoadingContainer(pedometerAvailabilityLoadingController) { availability ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            when (availability) {
                is PedometerManager.Availability.Available -> AvailablePedometerSection(
                    pedometerStatisticResolver = pedometerStatisticResolver,
                    millisDistanceFormatter = millisDistanceFormatter,
                    decimalFormatter = decimalFormatter,
                    chartEntriesLoadingController = chartEntriesLoadingController,
                    dailyRateStepsLoadingController = dailyRateStepsLoadingController,
                    todayStatisticLoadingController = todayStatisticLoadingController,
                    statisticLoadingController = statisticLoadingController,
                    pedometerToggleController = pedometerToggleController,
                    dailyProgressLoadingController = dailyRateProgressController,
                    rewardLoadingController = rewardLoadingController,
                    collectRewardController = collectRewardController,
                )

                is PedometerManager.Availability.NotAvailable -> {
                    PermissionsSection(
                        animationRepeatTimes = 1,
                        initial = Initial(
                            animationResId = R.raw.permissions,
                            titleResId = R.string.pedometer_permissions_not_granted_title_text,
                        ),
                        onGrant = pedometerToggleController::toggle,
                        rejected = pedometerRejectedMapper.mapReasonToRejected(availability.rejectReason),
                    )
                }
            }
        }
    }
}

@Composable
private fun AvailablePedometerSection(
    pedometerStatisticResolver: PedometerStatisticResolver,
    millisDistanceFormatter: MillisDistanceFormatter,
    decimalFormatter: DecimalFormatter,
    chartEntriesLoadingController: LoadingController<PedometerChartData>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    dailyProgressLoadingController: LoadingController<Float>,
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    statisticLoadingController: LoadingController<PedometerStatistic>,
    pedometerToggleController: ToggleController,
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
) {
    DailyRateSection(
        todayStatisticLoadingController = todayStatisticLoadingController,
        dailyRateStepsLoadingController = dailyRateStepsLoadingController,
        pedometerToggleController = pedometerToggleController,
        dailyRateProgressController = dailyProgressLoadingController,
    )

    Spacer(modifier = Modifier.height(32.dp))

    LoadingContainer(
        controller1 = chartEntriesLoadingController,
        controller2 = todayStatisticLoadingController,
        controller3 = statisticLoadingController,
        controller4 = rewardLoadingController,
    ) { chartData, todayStatistic, statistic, totalReward ->
        if (todayStatistic.totalSteps > 0) {
            PedometerInfoSection(
                millisDistanceFormatter = millisDistanceFormatter,
                decimalFormatter = decimalFormatter,
                statistic = todayStatistic,
            )
            Spacer(modifier = Modifier.height(32.dp))
            RequestButtonWithIcon(
                requestButtonConfig = RequestButtonConfig.Filled(
                    labelResId = R.string.currency_collectReward,
                    formatArgs = listOf(totalReward),
                    controller = collectRewardController,
                    iconResId = R.drawable.ic_money,
                ),
            )
            Spacer(modifier = Modifier.height(32.dp))
            StatisticsSection(
                statistics = statistic,
                pedometerStatisticResolver = pedometerStatisticResolver,
            )
            Spacer(modifier = Modifier.height(32.dp))
            ActivityChartSection(pedometerChartData = chartData)
        } else {
            EmptySection(emptyTitleResId = R.string.pedometer_nowEmpty_text)
        }
    }
}

@Composable
private fun DailyRateSection(
    todayStatisticLoadingController: LoadingController<PedometerStatistic>,
    dailyRateStepsLoadingController: LoadingController<Int>,
    dailyRateProgressController: LoadingController<Float>,
    pedometerToggleController: ToggleController,
) {
    LoadingContainer(
        controller1 = todayStatisticLoadingController,
        controller2 = dailyRateStepsLoadingController,
        controller3 = dailyRateProgressController,
    ) { todayStatistic, dailyRateSteps, progress ->
        val pedometerTrackingState by pedometerToggleController.state.collectAsState()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Headline(
                text = stringResource(
                    id = R.string.pedometer_stepCountFormat_text,
                    todayStatistic.totalSteps,
                    dailyRateSteps,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = todayStatistic.totalSteps < dailyRateSteps) {
                CircleIconButton(
                    circleIconButtonConfig = CircleIconButtonConfig.Filled(
                        onClick = pedometerToggleController::toggle,
                        iconResId = if (pedometerTrackingState.isActive) R.drawable.ic_stop else R.drawable.ic_play,
                        contentDescription = if (pedometerTrackingState.isActive) {
                            R.string.pedometer_stopIcon_contentDescription
                        } else {
                            R.string.pedometer_playIcon_contentDescription
                        },
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressBar(indicatorProgress = progress)
    }
}

@Composable
private fun PedometerInfoSection(
    statistic: PedometerStatistic,
    millisDistanceFormatter: MillisDistanceFormatter,
    decimalFormatter: DecimalFormatter,
) {
    Title(text = stringResource(id = R.string.pedometer_yourIndicatorsForThisDay_text))
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
                        iconResId = R.drawable.ic_my_location,
                        nameResId = R.string.pedometer_kilometersLabel_text,
                        value = decimalFormatter.roundAndFormatToString(statistic.totalKilometers),
                    )
                    PedometerInfoCard(
                        iconResId = R.drawable.ic_fire,
                        nameResId = R.string.pedometer_caloriesLabel_text,
                        value = decimalFormatter.roundAndFormatToString(statistic.totalCalories),
                    )
                    PedometerInfoCard(
                        iconResId = R.drawable.ic_time,
                        nameResId = R.string.pedometer_timeLabel_text,
                        value = millisDistanceFormatter.formatMillisDistance(
                            distanceInMillis = statistic.totalDuration.inWholeMilliseconds,
                            accuracy = MillisDistanceFormatter.Accuracy.MINUTES,
                        ),
                    )
                }
            },
        ),
    )
}

@Composable
private fun StatisticsSection(
    statistics: PedometerStatistic,
    pedometerStatisticResolver: PedometerStatisticResolver,
) {
    Title(text = stringResource(id = R.string.analytics_generalStatistics_text))
    Spacer(modifier = Modifier.height(16.dp))
    Statistics(statistics = pedometerStatisticResolver.resolve(statistics))
}

@Composable
private fun ActivityChartSection(pedometerChartData: PedometerChartData) {
    Title(text = stringResource(id = R.string.pedometer_analytics_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (pedometerChartData.entriesList.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(400.dp),
            chartEntries = pedometerChartData.entriesList.map { it.from to it.to },
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
        )
    } else {
        Description(text = stringResource(id = R.string.analytics_chartNotEnoughData_text))
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun PedometerPreview() {
    val context = LocalContext.current
    val millisDistanceFormatter = MillisDistanceFormatter(
        context = LocalContext.current,
        defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
    )
    val decimalFormatter = DecimalFormatter()

    HealtherTheme {
        Pedometer(
            onGoBack = {},
            onGoToHistory = {},
            millisDistanceFormatter = millisDistanceFormatter,
            decimalFormatter = decimalFormatter,
            pedometerStatisticResolver = PedometerStatisticResolver(
                context = context,
                millisDistanceFormatter = millisDistanceFormatter,
                decimalFormatter = decimalFormatter,
            ),
            pedometerRejectedMapper = PedometerRejectedMapper(),
            collectRewardController = MockControllersProvider.requestController(),
            rewardLoadingController = MockControllersProvider.loadingController(20.0),
            dailyRateProgressController = MockControllersProvider.loadingController(40.0f),
            dailyRateStepsLoadingController = MockControllersProvider.loadingController(14_000),
            pedometerToggleController = MockControllersProvider.toggleController(),
            pedometerAvailabilityLoadingController = MockControllersProvider.loadingController(PedometerManager.Availability.Available),
            statisticLoadingController = MockControllersProvider.loadingController(
                data = PedometerMockDataProvider.pedometerStatistics(),
            ),
            todayStatisticLoadingController = MockControllersProvider.loadingController(
                data = PedometerMockDataProvider.pedometerStatistics(),
            ),
            chartEntriesLoadingController = MockControllersProvider.loadingController(
                data = PedometerMockDataProvider.pedometerChartData(),
            ),
        )
    }
}