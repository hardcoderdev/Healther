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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.math.safeDiv
import hardcoder.dev.presentation.dashboard.DashboardItem
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.LoadingContainer
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
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun Dashboard(
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onGoToSettings: () -> Unit
) {
    val viewModel = koinViewModel<DashboardViewModel>()

    ScaffoldWrapper(
        content = {
            DashboardContent(
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                itemsLoadingController = viewModel.itemsLoadingController,
                pedometerToggleController = viewModel.pedometerToggleController
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
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    itemsLoadingController: LoadingController<List<DashboardItem>>,
    pedometerToggleController: ToggleController
) {
    LoadingContainer(controller = itemsLoadingController) { items ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
        ) {
            // TODO USER CHARACTER SECTION WILL BE HERE
            featureSection(
                items = items,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onTogglePedometerTrackingService = pedometerToggleController::toggle,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature
            )
        }
    }
}

private fun LazyListScope.featureSection(
    items: List<DashboardItem>,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit
) {
    items(items) { feature ->
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
                            waterTrackingFeature.millilitersDrunk.millilitersDrunkCount,
                            waterTrackingFeature.millilitersDrunk.dailyWaterIntake
                        )
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressBar(
                    progress = waterTrackingFeature.millilitersDrunk.millilitersDrunkCount safeDiv waterTrackingFeature.millilitersDrunk.dailyWaterIntake,
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
    val toggleButtonVisibilityCondition = pedometerFeature.stepsWalked <=
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Description(
                        text = stringResource(
                            id = R.string.dashboard_pedometer_progress_format,
                            formatArgs = arrayOf(
                                pedometerFeature.stepsWalked,
                                pedometerFeature.dailyRateInSteps
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressBar(
                        progress = pedometerFeature.stepsWalked safeDiv pedometerFeature.dailyRateInSteps,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .height(10.dp)
                    )
                    if (!toggleButtonVisibilityCondition) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(text = stringResource(id = R.string.dashboard_pedometer_permissions_not_granted))
                    }
                }
            }
            AnimatedVisibility(
                visible = toggleButtonVisibilityCondition,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
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
    val dateTimeFormatter = koinInject<DateTimeFormatter>()

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
                    fastingFeature.timeLeftDuration?.let { timeLeftInMillis ->
                        if (timeLeftInMillis.inWholeHours > requireNotNull(fastingFeature.planDuration).inWholeHours) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Description(text = stringResource(id = R.string.dashboard_fasting_completed))
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Description(
                                text = dateTimeFormatter.formatMillisDistance(
                                    distanceInMillis = timeLeftInMillis.inWholeMilliseconds,
                                    accuracy = DateTimeFormatter.Accuracy.SECONDS,
                                    usePlurals = true
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressBar(
                                progress = timeLeftInMillis.inWholeHours safeDiv requireNotNull(
                                    fastingFeature.planDuration
                                ).inWholeHours,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .height(10.dp)
                            )
                        }
                    } ?: run {
                        Spacer(modifier = Modifier.height(8.dp))
                        Description(text = stringResource(id = R.string.dashboard_fasting_is_not_active))
                    }
                }
            }
            fastingFeature.timeLeftDuration?.let {
                if (it.inWholeHours > requireNotNull(fastingFeature.planDuration).inWholeHours) {
                    ButtonWithIcon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        labelResId = R.string.dashboard_finish_fasting,
                        style = ButtonStyles.OUTLINED,
                        iconResId = R.drawable.ic_save,
                        onClick = onGoToFeature
                    )
                }
            }
            AnimatedVisibility(visible = fastingFeature.timeLeftDuration == null) {
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