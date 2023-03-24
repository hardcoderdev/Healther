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
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalFloatFormatter
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.extensions.safeDiv
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.pedometer.PedometerViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ActivityChartSection
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.icons.IconButton
import hardcoder.dev.uikit.progressBar.LinearProgressBar
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline

const val MINIMUM_ENTRIES_FOR_SHOWING_CHART = 2

@Composable
fun PedometerScreen(
    onGoBack: () -> Unit,
    onGoToPedometerHistory: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.createPedometerViewModel() }
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
                            iconResId = R.drawable.ic_more,
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
    val floatFormatter = LocalFloatFormatter.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DailyRateSection(
            state = state,
            onTogglePedometerTrackingService = onTogglePedometerTrackingService
        )
        Spacer(modifier = Modifier.height(64.dp))
        PedometerInfoSection(
            infoItemList = listOf(
                InfoItem(
                    iconResId = R.drawable.ic_my_location,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = floatFormatter.format(state.totalKilometersCount)
                ),
                InfoItem(
                    iconResId = R.drawable.ic_fire,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = floatFormatter.format(state.totalCaloriesBurned)
                ),
                InfoItem(
                    iconResId = R.drawable.ic_time,
                    nameResId = R.string.pedometer_timeLabel_text,
                    value = dateTimeFormatter.formatMillisDistance(state.totalTrackingTime)
                )
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        if (
            state.chartEntries.isNotEmpty() &&
            state.chartEntries.count() >= MINIMUM_ENTRIES_FOR_SHOWING_CHART
        ) {
            ActivityChartSection(
                modifier = Modifier.weight(2f),
                chartEntries = state.chartEntries
            )
        } else {
            Description(text = stringResource(id = R.string.pedometer_weDontHaveEnoughDataToShowChart))
        }
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