package hardcoder.dev.screens.dashboard.featureItems

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.mock.dataProviders.DashboardMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FoodTrackingFeatureItem(
    foodTrackingFeature: DashboardFeatureItem.FoodTrackingFeature,
    onGoToFeature: () -> Unit,
    onCreateFoodTrack: () -> Unit,
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
                            imageResId = R.drawable.dashboard_feature_fasting,
                            contentDescription = R.string.dashboard_food_tracking_feature,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ProgressSection(foodTrackingFeature = foodTrackingFeature)
                    }
                    QuickActions(onCreateFoodTrack = onCreateFoodTrack)
                }
            },
        ),
    )
}

@Composable
private fun ProgressSection(foodTrackingFeature: DashboardFeatureItem.FoodTrackingFeature) {
    Column {
        Title(text = stringResource(id = R.string.dashboard_food_tracking_feature))
        Spacer(modifier = Modifier.height(8.dp))  //TODO
        /* Description(
             text = stringResource(
                 id = R.string.dashboard_water_tracking_progress_format,
                 formatArgs = arrayOf(
                     waterTrackingFeature.millilitersDrunk.millilitersDrunkCount,
                     waterTrackingFeature.millilitersDrunk.dailyWaterIntake,
                 ),
             ),
         )
         Spacer(modifier = Modifier.height(12.dp))
         LinearProgressBar(
             indicatorProgress = waterTrackingFeature.progress,
             modifier = Modifier
                 .fillMaxWidth()
                 .clip(RoundedCornerShape(14.dp))
                 .height(10.dp),
         )*/
    }
}

@Composable
private fun BoxScope.QuickActions(
    onCreateFoodTrack: () -> Unit,
) {
    CircleIconButton(
        circleIconButtonConfig = CircleIconButtonConfig.Outlined(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onCreateFoodTrack,
            iconResId = R.drawable.ic_create,
            contentDescription = R.string.tracking_creation_title_topBar,
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun FoodTrackingFeatureItemPreview() {
    HealtherTheme {
        FoodTrackingFeatureItem(
            onGoToFeature = {},
            onCreateFoodTrack = {},
            foodTrackingFeature = DashboardMockDataProvider.dashboardFoodTrackingFeature(),
        )
    }
}