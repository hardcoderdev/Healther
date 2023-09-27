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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.mock.dataProviders.DashboardMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun DiaryFeatureItem(
    diaryFeature: DashboardFeatureItem.DiaryFeature,
    onGoToFeature: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
) {
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
                            imageResId = R.drawable.dashboard_feature_diary,
                            contentDescription = R.string.dashboard_diary_feature,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ProgressSection(diaryFeature = diaryFeature)
                    }
                    QuickActions(
                        isDailyRateClosed = diaryFeature.tracksCount >= diaryFeature.tracksDailyRate,
                        onCreateDiaryTrack = onCreateDiaryTrack,
                    )
                }
            },
        ),
    )
}

@Composable
private fun ProgressSection(diaryFeature: DashboardFeatureItem.DiaryFeature) {
    Column {
        Title(text = stringResource(id = R.string.dashboard_diary_feature))
        Spacer(modifier = Modifier.height(8.dp))
        Description(
            text = stringResource(
                id = R.string.dashboard_diary_tracking_progress_format,
                formatArgs = arrayOf(
                    diaryFeature.tracksCount,
                    pluralStringResource(id = R.plurals.entries, count = diaryFeature.tracksCount),
                    diaryFeature.tracksDailyRate,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressBar(
            indicatorProgress = diaryFeature.progress,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .height(10.dp),
        )
    }
}

@Composable
private fun BoxScope.QuickActions(
    isDailyRateClosed: Boolean,
    onCreateDiaryTrack: () -> Unit,
) {
    AnimatedVisibility(
        visible = !isDailyRateClosed,
        modifier = Modifier.align(Alignment.TopEnd),
    ) {
        CircleIconButton(
            circleIconButtonConfig = CircleIconButtonConfig.Outlined(
                onClick = onCreateDiaryTrack,
                iconResId = R.drawable.ic_create,
                contentDescription = R.string.dashboard_diaryCreationQuickAction_contentDescription,
            ),
        )
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryFeatureItemPreview() {
    HealtherTheme {
        DiaryFeatureItem(
            onGoToFeature = {},
            onCreateDiaryTrack = {},
            diaryFeature = DashboardMockDataProvider.dashboardDiaryFeature(),
        )
    }
}