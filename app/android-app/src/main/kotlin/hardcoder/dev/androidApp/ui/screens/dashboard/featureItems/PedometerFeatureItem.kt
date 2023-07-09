package hardcoder.dev.androidApp.ui.screens.dashboard.featureItems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.presentation.dashboard.DashboardItem
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.app.android.app.R

@Composable
fun PedometerFeatureItem(
    pedometerFeature: DashboardItem.PedometerFeature,
    onGoToFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
) {
    val isToggleButtonVisible = pedometerFeature.stepsWalked <=
        pedometerFeature.dailyRateInSteps && pedometerFeature.permissionsGranted

    Card(
        cardConfig = CardConfig.Action(
            modifier = Modifier.wrapContentHeight(),
            onClick = onGoToFeature,
            cardContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Row {
                        Image(
                            modifier = Modifier.size(60.dp),
                            imageResId = R.drawable.dashboard_feature_pedometer,
                            contentDescription = R.string.dashboard_pedometer_feature,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ProgressSection(
                            pedometerFeature = pedometerFeature,
                            isToggleButtonVisible = isToggleButtonVisible,
                        )
                    }
                    QuickActions(
                        pedometerFeature = pedometerFeature,
                        onTogglePedometerTrackingService = onTogglePedometerTrackingService,
                        isToggleButtonVisible = isToggleButtonVisible,
                    )
                }
            },
        ),
    )
}

@Composable
private fun ProgressSection(
    pedometerFeature: DashboardItem.PedometerFeature,
    isToggleButtonVisible: Boolean,
) {
    Column {
        Title(text = stringResource(id = R.string.dashboard_pedometer_feature))
        Spacer(modifier = Modifier.height(8.dp))
        Description(
            text = stringResource(
                id = R.string.dashboard_pedometer_progress_format,
                formatArgs = arrayOf(
                    pedometerFeature.stepsWalked,
                    pedometerFeature.dailyRateInSteps,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressBar(
            progress = pedometerFeature.progress,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .height(10.dp),
        )
        if (!isToggleButtonVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            Description(text = stringResource(id = R.string.dashboard_pedometer_permissions_not_granted))
        }
    }
}

@Composable
private fun BoxScope.QuickActions(
    isToggleButtonVisible: Boolean,
    onTogglePedometerTrackingService: () -> Unit,
    pedometerFeature: DashboardItem.PedometerFeature,
) {
    AnimatedVisibility(
        visible = isToggleButtonVisible,
        modifier = Modifier.align(Alignment.TopEnd),
    ) {
        CircleIconButton(
            circleIconButtonConfig = CircleIconButtonConfig.Outlined(
                onClick = onTogglePedometerTrackingService,
                iconResId = if (pedometerFeature.isPedometerRunning) R.drawable.ic_stop else R.drawable.ic_play,
                contentDescription = if (pedometerFeature.isPedometerRunning) {
                    R.string.pedometer_stopIcon_contentDescription
                } else {
                    R.string.pedometer_playIcon_contentDescription
                },
            ),
        )
    }
}