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
import hardcoder.dev.entities.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.extensions.roundAndFormatToString
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.pedometer.Available
import hardcoder.dev.presentation.features.pedometer.NotAvailable
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Statistics
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.card.CardInfo
import hardcoder.dev.uikit.card.CardInfoItem
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
    val state by viewModel.state.collectAsState()

    when (val loadingState = state) {
        is PedometerViewModel.LoadingState.Loaded -> {
            ScaffoldWrapper(
                content = {
                    PedometerContent(
                        state = loadingState.state,
                        onTogglePedometerTrackingService = viewModel::togglePedometerTracking
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

        is PedometerViewModel.LoadingState.Loading -> {
            /* no-op */
        }
    }
}

@Composable
private fun PedometerContent(
    state: PedometerViewModel.State,
    onTogglePedometerTrackingService: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val pedometerRejectedMapper = uiModule.pedometerRejectedMapper

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when (val availability = state.availability) {
            is Available -> AvailablePedometerSection(
                state = state,
                onTogglePedometerTrackingService = onTogglePedometerTrackingService
            )

            is NotAvailable -> {
                PermissionsSection(
                    animationRepeatTimes = 1,
                    initial = Initial(
                        animationResId = R.raw.permissions,
                        titleResId = R.string.pedometer_permissions_not_granted_title_text,
                    ),
                    onGrant = onTogglePedometerTrackingService,
                    rejected = pedometerRejectedMapper.mapReasonToRejected(
                        rejectReason = availability.rejectReason,
                        isInitialScreenShowed = state.initialPermissionScreenShowed
                    )
                )
            }
        }
    }
}

@Composable
private fun AvailablePedometerSection(
    state: PedometerViewModel.State,
    onTogglePedometerTrackingService: () -> Unit
) {
    DailyRateSection(
        state = state,
        onTogglePedometerTrackingService = onTogglePedometerTrackingService
    )

    Spacer(modifier = Modifier.height(32.dp))
    if (state.pedometerStatistic != null) {
        PedometerInfoSection(state = state)
        Spacer(modifier = Modifier.height(32.dp))
        PedometerStatisticSection(statistic = requireNotNull(state.pedometerStatistic))
        Spacer(modifier = Modifier.height(32.dp))
        PedometerActivityChart(state = state)
    } else {
        EmptySection(emptyTitleResId = R.string.pedometer_nowEmpty_text)
    }
}

@Composable
private fun DailyRateSection(
    state: PedometerViewModel.State,
    onTogglePedometerTrackingService: () -> Unit
) {
    val isPedometerRunning = state.isTrackingNow

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Headline(
            text = stringResource(
                id = R.string.pedometer_stepCountFormat_text,
                state.totalStepsCount,
                state.dailyRateStepsCount
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = state.totalStepsCount <= state.dailyRateStepsCount) {
            IconButton(
                onClick = onTogglePedometerTrackingService,
                iconResId = if (isPedometerRunning) R.drawable.ic_stop else R.drawable.ic_play,
                contentDescription = if (isPedometerRunning) {
                    R.string.pedometer_stopIcon_contentDescription
                } else {
                    R.string.pedometer_playIcon_contentDescription
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    LinearProgressBar(progress = state.totalStepsCount safeDiv state.dailyRateStepsCount)
}

@Composable
private fun PedometerInfoSection(state: PedometerViewModel.State) {
    val uiModule = LocalUIModule.current
    val dateTimeFormatter = uiModule.dateTimeFormatter

    Title(text = stringResource(id = R.string.pedometer_yourIndicatorsForThisDay_text))
    Spacer(modifier = Modifier.height(16.dp))
    Card<CardInfo>(interactionType = InteractionType.STATIC) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
            CardInfoItem(
                iconResId = R.drawable.ic_time,
                nameResId = R.string.pedometer_timeLabel_text,
                value = dateTimeFormatter.formatMillisDistance(
                    distanceInMillis = state.totalTrackingTime,
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
private fun PedometerActivityChart(state: PedometerViewModel.State) {
    Title(text = stringResource(id = R.string.waterTracking_activity_chart))
    Spacer(modifier = Modifier.height(16.dp))
    if (state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART) {
        ActivityLineChart(
            modifier = Modifier.height(400.dp),
            chartEntries = state.chartEntries,
            xAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            },
            yAxisValueFormatter = { value, _ ->
                value.roundToInt().toString()
            }
        )
    } else {
        Description(text = stringResource(id = R.string.pedometer_weDontHaveEnoughDataToShowChart_text))
    }
}

