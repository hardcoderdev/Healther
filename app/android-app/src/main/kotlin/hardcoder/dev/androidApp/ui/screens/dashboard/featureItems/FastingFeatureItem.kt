package hardcoder.dev.androidApp.ui.screens.dashboard.featureItems

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun FastingFeatureItem(
    fastingFeature: DashboardFeatureItem.FastingFeature,
    onGoToFeature: () -> Unit,
    onStartFasting: () -> Unit,
) {
    Card(
        cardConfig = CardConfig.Action(
            modifier = Modifier.wrapContentHeight(),
            onClick = onGoToFeature,
            cardContent = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(60.dp),
                            imageResId = R.drawable.dashboard_feature_fasting,
                            contentDescription = R.string.dashboard_fasting_feature,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        InfoSection(fastingFeature = fastingFeature)
                    }
                    QuickActionsSection(
                        fastingFeature = fastingFeature,
                        onGoToFeature = onGoToFeature,
                        onStartFasting = onStartFasting,
                    )
                }
            },
        ),
    )
}

@Composable
private fun InfoSection(
    fastingFeature: DashboardFeatureItem.FastingFeature,
) {
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

    Column(Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.dashboard_fasting_feature))
        fastingFeature.timeLeftDuration?.let { timeLeftInMillis ->
            if (timeLeftInMillis.inWholeMilliseconds > fastingFeature.planDuration!!.inWholeMilliseconds) {
                Spacer(modifier = Modifier.height(8.dp))
                Description(text = stringResource(id = R.string.dashboard_fasting_completed))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Description(
                    text = millisDistanceFormatter.formatMillisDistance(
                        distanceInMillis = timeLeftInMillis.inWholeMilliseconds,
                        accuracy = MillisDistanceFormatter.Accuracy.SECONDS,
                        usePlurals = true,
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressBar(
                    indicatorProgress = fastingFeature.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .height(10.dp),
                )
            }
        } ?: run {
            Spacer(modifier = Modifier.height(8.dp))
            Description(text = stringResource(id = R.string.dashboard_fasting_is_not_active))
        }
    }
}

@Composable
private fun QuickActionsSection(
    fastingFeature: DashboardFeatureItem.FastingFeature,
    onGoToFeature: () -> Unit,
    onStartFasting: () -> Unit,
) {
    fastingFeature.timeLeftDuration?.let {
        if (it.inWholeHours > requireNotNull(fastingFeature.planDuration).inWholeHours) {
            TextIconButton(
                textIconButtonConfig = TextIconButtonConfig.Outlined(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    labelResId = R.string.dashboard_finish_fasting,
                    iconResId = R.drawable.ic_save,
                    onClick = onGoToFeature,
                ),
            )
        }
    }
    AnimatedVisibility(visible = fastingFeature.timeLeftDuration == null) {
        TextIconButton(
            textIconButtonConfig = TextIconButtonConfig.Outlined(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
                labelResId = R.string.dashboard_start_fasting,
                iconResId = R.drawable.ic_play,
                onClick = onStartFasting,
            ),
        )
    }
}