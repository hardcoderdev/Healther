@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.android_ui.features.pedometer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.extensions.hasPermission
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

@Composable
fun PedometerScreen(
    onGoBack: () -> Unit,
    onGoToPedometerHistory: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val context = LocalContext.current

    val viewModel = viewModel { presentationModule.createPedometerViewModel() }
    val state = viewModel.state.collectAsState()

    val serviceIntent = Intent(context, PedometerService::class.java)

    ScaffoldWrapper(
        content = {
            PedometerContent(
                state = state.value,
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
                titleResId = R.string.pedometerScreen_title_topBar,
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

@Composable
private fun PedometerContent(
    state: PedometerViewModel.State,
    onStartPedometerService: () -> Unit,
    onStopPedometerService: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DailyRateSection(
            state = state,
            onStartPedometerService = onStartPedometerService,
            onStopPedometerService = onStopPedometerService
        )
        Spacer(modifier = Modifier.height(64.dp))
        InfoForTodayCardSection(state = state)
        Spacer(modifier = Modifier.height(32.dp))
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
            R.string.pedometerScreen_stopIcon_contentDescription
        } else {
            R.string.pedometerScreen_playIcon_contentDescription
        }
    )

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            if (isPedometerRunning) {
                onStopPedometerService()
            } else {
                onStartPedometerService()
            }
        } else {
            context.toast(msgResId = R.string.pedometerScreen_thisPermissionIsRequired_error)
        }
    }

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
                    id = R.string.pedometerScreen_stepCountFormat_text,
                    state.totalStepsCount,
                    state.dailyRateStepsCount
                ),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            // AnimatedVisibility(visible = state.totalStepsCount >= state.dailyRateStepsCount) {
            IconButton(
                onClick = {
                    if (context.hasPermission(Manifest.permission.ACTIVITY_RECOGNITION)) {
                        if (isPedometerRunning) {
                            onStopPedometerService()
                        } else {
                            onStartPedometerService()
                        }
                    } else {
                        launcher.launch(
                            arrayOf(
                                Manifest.permission.ACTIVITY_RECOGNITION,
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        )
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
            // }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    LinearProgressIndicator(
        progress = (state.totalStepsCount.toFloat() / state.dailyRateStepsCount.toFloat())
            .toString().substring(0, 3).toFloat(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .height(16.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun InfoForTodayCardSection(state: PedometerViewModel.State) {
    Text(
        text = stringResource(id = R.string.pedometerScreen_yourIndicatorsForToday_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        onClick = {},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemInfo(
                imageVector = Icons.Filled.MyLocation,
                valueLabelResId = R.string.pedometerScreen_kilometersLabel_text,
                value = state.totalKilometersCount
            )
            ItemInfo(
                imageVector = Icons.Filled.Fireplace,
                valueLabelResId = R.string.pedometerScreen_caloriesLabel_text,
                value = state.totalCaloriesBurned
            )
            ItemInfo(
                imageVector = Icons.Filled.LockClock,
                valueLabelResId = R.string.pedometerScreen_timeLabel_text,
                value = 0.0f
            )
        }
    }
}

@Composable
private fun ItemInfo(imageVector: ImageVector, @StringRes valueLabelResId: Int, value: Float) {
    Column {
        Icon(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = stringResource(id = valueLabelResId),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
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
                    totalCaloriesBurned = 60.2f
                ),
                onStartPedometerService = {},
                onStopPedometerService = {}
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.pedometerScreen_title_topBar,
                onGoBack = {

                }
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconImageVector = Icons.Filled.MoreVert,
                    onActionClick = {

                    }
                )
            )
        )
    )
}