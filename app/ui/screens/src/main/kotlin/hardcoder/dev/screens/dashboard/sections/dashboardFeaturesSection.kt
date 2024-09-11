package hardcoder.dev.screens.dashboard.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.blocks.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.DiaryFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.FoodTrackingFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.MoodTrackingFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.PedometerFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.WaterTrackingFeatureItem
import hardcoderdev.healther.app.ui.resources.R

fun LazyListScope.dashboardFeaturesSection(
    items: List<DashboardFeatureItem>,
    onCustomizeFeatures: () -> Unit,
    onGoToWaterTrackingFeature: () -> Unit,
    onCreateWaterTrack: () -> Unit,
    onGoToFoodTrackingFeature: () -> Unit,
    onCreateFoodTrack: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToDiary: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
) {
    item {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Title(text = stringResource(R.string.dashboard_your_features))
            CircleIconButton(
                circleIconButtonConfig = CircleIconButtonConfig.Outlined(
                    onClick = onCustomizeFeatures,
                    iconResId = R.drawable.ic_customize,
                    contentDescription = R.string.dashboard_customize_features_cd,
                    modifier = Modifier.size(32.dp),
                ),
            )
        }
    }
    items(items) { feature ->
        when (feature) {
            is DashboardFeatureItem.WaterTrackingFeature -> {
                WaterTrackingFeatureItem(
                    waterTrackingFeature = feature,
                    onGoToFeature = onGoToWaterTrackingFeature,
                    onCreateWaterTrack = onCreateWaterTrack,
                )
            }

            is DashboardFeatureItem.FoodTrackingFeature -> {
                FoodTrackingFeatureItem(
                    foodTrackingFeature = feature,
                    onGoToFeature = onGoToFoodTrackingFeature,
                    onCreateFoodTrack = onCreateFoodTrack,
                )
            }

            is DashboardFeatureItem.MoodTrackingFeature -> {
                MoodTrackingFeatureItem(
                    moodTrackingFeature = feature,
                    onGoToFeature = onGoToMoodTrackingFeature,
                    onCreateMoodTrack = onCreateMoodTrack,
                )
            }

            is DashboardFeatureItem.PedometerFeature -> {
                PedometerFeatureItem(
                    pedometerFeature = feature,
                    onGoToFeature = onGoToPedometerFeature,
                    onTogglePedometerTrackingService = onTogglePedometerTrackingService,
                )
            }

            is DashboardFeatureItem.DiaryFeature -> {
                DiaryFeatureItem(
                    diaryFeature = feature,
                    onGoToFeature = onGoToDiary,
                    onCreateDiaryTrack = onCreateDiaryTrack,
                )
            }
        }
    }
}