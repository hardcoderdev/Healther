package hardcoder.dev.androidApp.ui.screens.dashboard.featureItems

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.presentation.dashboard.DashboardItem
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.text.Description
import hardcoderdev.healther.app.android.app.R

@Composable
fun DiaryFeatureItem(
    diaryFeature: DashboardItem.DiaryFeature,
    onGoToFeature: () -> Unit,
) {
    Card(
        cardConfig = CardConfig.Action(
            modifier = Modifier.wrapContentHeight(),
            onClick = onGoToFeature,
            cardContent = {
                FeatureItemContent(
                    nameResId = R.string.dashboard_diary_feature,
                    imageResId = R.drawable.dashboard_feature_diary,
                    content = {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (diaryFeature.dailyRate == 0) {
                            Description(text = stringResource(id = R.string.dashboard_diary_noTracks))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressBar(
                            progress = diaryFeature.progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .height(10.dp),
                        )
                    },
                )
            },
        ),
    )
}