package hardcoder.dev.androidApp.ui.screens.dashboard.featureItems

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.presentation.dashboard.DashboardItem
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Description
import hardcoderdev.healther.app.android.app.R

@Composable
fun MoodTrackingFeatureItem(
    moodTrackingFeature: DashboardItem.MoodTrackingFeature,
    onGoToFeature: () -> Unit,
) {
    Card(
        cardConfig = CardConfig.Action(
            modifier = Modifier.wrapContentHeight(),
            onClick = onGoToFeature,
            cardContent = {
                FeatureItemContent(
                    nameResId = R.string.dashboard_mood_tracking_feature,
                    imageResId = R.drawable.dashboard_feature_mood_tracking,
                    content = {
                        moodTrackingFeature.averageMoodToday?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Description(text = stringResource(id = R.string.dashboard_mood_tracking_progress_format))
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    imageResId = it.icon.resourceId,
                                    contentDescription = null,
                                )
                            }
                        } ?: run {
                            Spacer(modifier = Modifier.height(8.dp))
                            Description(text = stringResource(id = R.string.dashboard_mood_tracking_not_specified))
                        }
                    },
                )
            },
        ),
    )
}