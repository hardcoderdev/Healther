@file:OptIn(ExperimentalPermissionsApi::class)

package hardcoder.dev.android_ui.features.pedometer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import hardcoder.dev.android_ui.LocalFloatFormatter
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.extensions.toast
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.pedometer.PedometerViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Text
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.utilities.VersionChecker
import hardcoder.dev.presentation.pedometer.PedometerTrackItem as PedometerTrackItemEntry

@Composable
fun PedometerScreen(
    onGoBack: () -> Unit,
    onGoToPedometerHistory: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val context = LocalContext.current

    val viewModel = viewModel { presentationModule.createPedometerViewModel() }
    val state by viewModel.state.collectAsState()

    val serviceIntent = Intent(context, PedometerService::class.java)

    when (val loadingState = state) {
        is PedometerViewModel.LoadingState.Loaded -> {
            ScaffoldWrapper(
                content = {
                    PedometerContent(
                        state = loadingState.state,
                        onStartPedometerService = {
                            if (VersionChecker.isOreo()) {
                                context.startForegroundService(serviceIntent)
                            } else {
                                context.startService(serviceIntent)
                            }
                            viewModel.updateTrackingStatus(isTracking = true)
                        },
                        onStopPedometerService = {
                            context.stopService(serviceIntent)
                            viewModel.updateTrackingStatus(isTracking = false)
                        }
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
                            iconImageVector = Icons.Filled.MoreVert,
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
    onStartPedometerService: () -> Unit,
    onStopPedometerService: () -> Unit
) {
    val floatFormatter = LocalFloatFormatter.current

    val pedometerTrackItemEntry = PedometerTrackItemEntry(
        stepsCount = state.totalStepsCount,
        kilometersCount = state.totalKilometersCount,
        caloriesBurnt = state.totalCaloriesBurned
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DailyRateSection(
            state = state,
            onStartPedometerService = onStartPedometerService,
            onStopPedometerService = onStopPedometerService
        )
        Spacer(modifier = Modifier.height(64.dp))
        PedometerInfoSection(
            infoItemList = listOf(
                InfoItem(
                    icon = Icons.Filled.MyLocation,
                    nameResId = R.string.pedometer_kilometersLabel_text,
                    value = floatFormatter.format(pedometerTrackItemEntry.kilometersCount)
                ),
                InfoItem(
                    icon = Icons.Filled.Fireplace,
                    nameResId = R.string.pedometer_caloriesLabel_text,
                    value = floatFormatter.format(pedometerTrackItemEntry.caloriesBurnt)
                ),
                InfoItem(
                    icon = Icons.Filled.LockClock,
                    nameResId = R.string.pedometer_timeLabel_text,
                    value = "ВВОВЫЩ" // TODO
                )
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
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
                text = stringResource(id = R.string.pedometer_weDontHaveEnoughDataToShowChart),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun DailyRateSection(
    state: PedometerViewModel.State,
    onStartPedometerService: () -> Unit,
    onStopPedometerService: () -> Unit
) {
    val isPedometerRunning = state.isTrackingNow
    val toggleServiceButtonIcon =
        if (isPedometerRunning) Icons.Filled.Stop else Icons.Filled.PlayArrow
    val toggleServiceButtonContentDescription = stringResource(
        id = if (isPedometerRunning) {
            R.string.pedometer_stopIcon_contentDescription
        } else {
            R.string.pedometer_playIcon_contentDescription
        }
    )

    val context = LocalContext.current
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )

    val packageName = context.packageName
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
        Intent().apply {
            action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            flags = FLAG_ACTIVITY_NEW_TASK
            data = "package:$packageName".toUri()
        }.also {
            context.startActivity(it)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    id = R.string.pedometer_stepCountFormat_text,
                    state.totalStepsCount,
                    state.dailyRateStepsCount
                ),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = state.totalStepsCount <= state.dailyRateStepsCount) {
                IconButton(
                    onClick = {
                        permissionsState.permissions.forEach {
                            // TODO WHEN DENIED SEND USER TO SETTINGS
                            if (it.status.shouldShowRationale) {
                                context.toast(msgResId = R.string.pedometer_thisPermissionsIsRequired_error)
                            } else if (!it.status.isGranted) {
                                context.toast(msgResId = R.string.pedometer_thisPermissionsIsRequired_error)
                            }
                        }
                        if (permissionsState.permissions.all { it.status.isGranted }) {
                            if (isPedometerRunning) {
                                onStopPedometerService()
                            } else {
                                onStartPedometerService()
                            }
                        } else {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    }
                ) {
                    Icon(
                        imageVector = toggleServiceButtonIcon,
                        contentDescription = toggleServiceButtonContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(8.dp)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    LinearProgressIndicator(
        progress = (state.totalStepsCount.toFloat() / state.dailyRateStepsCount.toFloat()).toString()
            .substring(0, 3).toFloat(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .height(16.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview
@Composable
fun PedometerContentPreview() {
    ScaffoldWrapper(
        content = {
            PedometerContent(
                state = PedometerViewModel.State(
                    isTrackingNow = false,
                    totalStepsCount = 300,
                    dailyRateStepsCount = 3000,
                    totalKilometersCount = 10.0f,
                    totalCaloriesBurned = 60.2f,
                    totalWastedTime = 20,
                    chartEntries = listOf(0 to 0)
                ),
                onStartPedometerService = {},
                onStopPedometerService = {}
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometer_title_topBar,
                onGoBack = {}
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconImageVector = Icons.Filled.MoreVert,
                    onActionClick = {}
                )
            )
        )
    )
}