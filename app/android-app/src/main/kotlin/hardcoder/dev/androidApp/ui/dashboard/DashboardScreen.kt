package hardcoder.dev.androidApp.ui.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.DashboardItem
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.card.ActionCard
import hardcoder.dev.uikit.icons.IconButton
import hardcoder.dev.uikit.progressBar.LinearProgressBar
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title

@Composable
fun DashboardScreen(
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onGoToSettings: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getDashboardViewModel() }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            DashboardContent(
                state = state.value,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onTogglePedometerTrackingService = viewModel::onTogglePedometerTrackingService,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(
                titleResId = R.string.dashboard_title_topBar
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_settings,
                    onActionClick = onGoToSettings
                )
            )
        )
    )
}

@Composable
private fun DashboardContent(
    state: DashboardViewModel.State,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        // TODO USER CHARACTER SECTION WILL BE HERE
        featureSection(
            state = state,
            onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
            onGoToPedometerFeature = onGoToPedometerFeature,
            onTogglePedometerTrackingService = onTogglePedometerTrackingService,
            onGoToFastingFeature = onGoToFastingFeature,
            onStartFasting = onStartFasting,
            onGoToMoodTrackingFeature = onGoToMoodTrackingFeature
        )
    }
}


private fun LazyListScope.featureSection(
    state: DashboardViewModel.State,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit
) {
    item { Title(text = stringResource(id = R.string.dashboard_features_text)) }
    items(state.dashboardItems) { feature ->
        when (feature) {
            is DashboardItem.FastingFeature -> {
                FastingFeatureItem(
                    fastingFeature = feature,
                    onGoToFeature = onGoToFastingFeature,
                    onStartFasting = onStartFasting
                )
            }

            is DashboardItem.MoodTrackingFeature -> {
                MoodTrackingFeatureItem(
                    moodTrackingFeature = feature,
                    onGoToFeature = onGoToMoodTrackingFeature
                )
            }

            is DashboardItem.PedometerFeature -> {
                PedometerFeatureItem(
                    pedometerFeature = feature,
                    onGoToFeature = onGoToPedometerFeature,
                    onTogglePedometerTrackingService = onTogglePedometerTrackingService
                )
            }

            is DashboardItem.WaterTrackingFeature -> {
                WaterTrackingFeatureItem(
                    waterTrackingFeature = feature,
                    onGoToFeature = onGoToWaterTrackingFeature
                )
            }
        }
    }
}

@Composable
private fun WaterTrackingFeatureItem(
    waterTrackingFeature: DashboardItem.WaterTrackingFeature,
    onGoToFeature: () -> Unit
) {
    ActionCard(
        modifier = Modifier.wrapContentHeight(),
        onClick = onGoToFeature
    ) {
        FeatureItemContent(
            name = R.string.dashboard_water_tracking_feature,
            image = R.drawable.dashboard_feature_water_tracking,
            content = {
                    Spacer(modifier = Modifier.height(8.dp))
                    Description(
                        text = stringResource(
                            id = R.string.dashboard_water_tracking_progress_format,
                            formatArgs = arrayOf(
                                waterTrackingFeature.millilitersDrunk ?: 0,
                                waterTrackingFeature.dailyRateInMilliliters
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressBar(
                        progress = ((waterTrackingFeature.millilitersDrunk?.toFloat()
                            ?: (0.0f / waterTrackingFeature.dailyRateInMilliliters.toFloat())))
                            .toString()
                            .substring(0, 3).toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .height(10.dp)
                    )
            }
        )
    }
}

@Composable
private fun PedometerFeatureItem(
    pedometerFeature: DashboardItem.PedometerFeature,
    onGoToFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit
) {
    val toggleButtonVisibilityCondition = (pedometerFeature.stepsWalked ?: 0) <=
            pedometerFeature.dailyRateInSteps && pedometerFeature.permissionsGranted

    ActionCard(
        modifier = Modifier.wrapContentHeight(),
        onClick = onGoToFeature
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.dashboard_feature_pedometer),
                    contentDescription = stringResource(id = R.string.dashboard_pedometer_feature)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Title(text = stringResource(id = R.string.dashboard_pedometer_feature))
                    pedometerFeature.stepsWalked?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(
                            text = stringResource(
                                id = R.string.dashboard_pedometer_progress_format,
                                formatArgs = arrayOf(it, pedometerFeature.dailyRateInSteps)
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressBar(
                            progress = (it.toFloat() / pedometerFeature.dailyRateInSteps.toFloat())
                                .toString()
                                .substring(0, 3).toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .height(10.dp)
                        )
                    } ?: run {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(text = stringResource(id = R.string.dashboard_pedometer_permissions_not_granted))
                    }
                }
            }
            AnimatedVisibility(visible = toggleButtonVisibilityCondition, modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(
                    style = ButtonStyles.OUTLINED,
                    onClick = onTogglePedometerTrackingService,
                    iconResId = if (pedometerFeature.isPedometerRunning) R.drawable.ic_stop else R.drawable.ic_play,
                    contentDescription = if (pedometerFeature.isPedometerRunning) {
                        R.string.pedometer_stopIcon_contentDescription
                    } else {
                        R.string.pedometer_playIcon_contentDescription
                    }
                )
            }
        }
    }
}

@Composable
private fun FastingFeatureItem(
    fastingFeature: DashboardItem.FastingFeature,
    onGoToFeature: () -> Unit,
    onStartFasting: () -> Unit
) {
    ActionCard(
        modifier = Modifier.wrapContentHeight(),
        onClick = onGoToFeature
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.dashboard_feature_fasting),
                    contentDescription = stringResource(id = R.string.dashboard_fasting_feature)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(Modifier.fillMaxWidth()) {
                    Title(text = stringResource(id = R.string.dashboard_fasting_feature))
                    fastingFeature.hoursLeft?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(
                            text = stringResource(
                                id = R.string.dashboard_fasting_progress_format,
                                formatArgs = arrayOf(it, requireNotNull(fastingFeature.hoursInPlan))
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressBar(
                            progress = (it.toFloat() / requireNotNull(fastingFeature.hoursInPlan).toFloat())
                                .toString()
                                .substring(0, 3).toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .height(10.dp)
                        )
                    } ?: run {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(text = stringResource(id = R.string.dashboard_fasting_is_not_active))
                    }
                }
            }
            ButtonWithIcon(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                labelResId = R.string.dashboard_start_fasting,
                style = ButtonStyles.OUTLINED,
                iconResId = R.drawable.ic_play,
                onClick = onStartFasting
            )
        }
    }
}

@Composable
private fun MoodTrackingFeatureItem(
    moodTrackingFeature: DashboardItem.MoodTrackingFeature,
    onGoToFeature: () -> Unit
) {
    ActionCard(
        modifier = Modifier.wrapContentHeight(),
        onClick = onGoToFeature
    ) {
        FeatureItemContent(
            name = R.string.dashboard_mood_tracking_feature,
            image = R.drawable.dashboard_feature_mood_tracking,
            content = {
                moodTrackingFeature.averageMoodToday?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Description(text = stringResource(id = R.string.dashboard_mood_tracking_progress_format))
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(id = it.icon.resourceId),
                            contentDescription = it.name
                        )
                    }
                } ?: run {
                    Spacer(modifier = Modifier.height(8.dp))
                    Description(text = stringResource(id = R.string.dashboard_mood_tracking_not_specified))
                }
            }
        )
    }
}

@Composable
private fun FeatureItemContent(
    @StringRes name: Int,
    @DrawableRes image: Int,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(id = image),
            contentDescription = stringResource(id = name)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(Modifier.fillMaxWidth()) {
            Title(text = stringResource(id = name))
            content()
        }
    }
}