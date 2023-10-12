package hardcoder.dev.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.DashboardMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.DiaryFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.FoodTrackingFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.MoodTrackingFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.PedometerFeatureItem
import hardcoder.dev.screens.dashboard.featureItems.WaterTrackingFeatureItem
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun Dashboard(
    featureItemsLoadingController: LoadingController<List<DashboardFeatureItem>>,
    pedometerToggleController: ToggleController,
    onGoToWaterTrackingFeature: () -> Unit,
    onCreateWaterTrack: () -> Unit,
    onGoToFoodTrackingFeature: () -> Unit,
    onCreateFoodTrack: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToDiary: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
    onGoToSettings: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DashboardContent(
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onCreateWaterTrack = onCreateWaterTrack,
                onGoToFoodTrackingFeature = onGoToFoodTrackingFeature,
                onCreateFoodTrack = onCreateFoodTrack,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                onCreateMoodTrack = onCreateMoodTrack,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onGoToDiary = onGoToDiary,
                onCreateDiaryTrack = onCreateDiaryTrack,
                featureItemsLoadingController = featureItemsLoadingController,
                pedometerToggleController = pedometerToggleController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(
                titleResId = R.string.dashboard_title_topBar,
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_settings,
                    onActionClick = onGoToSettings,
                ),
            ),
        ),
    )
}

@Composable
private fun DashboardContent(
    onGoToWaterTrackingFeature: () -> Unit,
    onCreateWaterTrack: () -> Unit,
    onGoToFoodTrackingFeature: () -> Unit,
    onCreateFoodTrack: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToDiary: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
    featureItemsLoadingController: LoadingController<List<DashboardFeatureItem>>,
    pedometerToggleController: ToggleController,
) {
    LoadingContainer(controller = featureItemsLoadingController) { featureItems ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        ) {
            featureSection(
                items = featureItems,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onCreateWaterTrack = onCreateWaterTrack,
                onGoToFoodTrackingFeature = onGoToFoodTrackingFeature,
                onCreateFoodTrack = onCreateFoodTrack,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onTogglePedometerTrackingService = pedometerToggleController::toggle,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                onCreateMoodTrack = onCreateMoodTrack,
                onGoToDiary = onGoToDiary,
                onCreateDiaryTrack = onCreateDiaryTrack,
            )
        }
    }
}

private fun LazyListScope.featureSection(
    items: List<DashboardFeatureItem>,
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

@HealtherScreenPhonePreviews
@Composable
private fun DashboardPreview() {
    HealtherTheme {
        Dashboard(
            onGoToSettings = {},
            onGoToWaterTrackingFeature = {},
            onCreateWaterTrack = {},
            onGoToFoodTrackingFeature = {},
            onCreateFoodTrack = {},
            onGoToMoodTrackingFeature = {},
            onCreateMoodTrack = {},
            onGoToPedometerFeature = {},
            onGoToDiary = {},
            onCreateDiaryTrack = {},
            pedometerToggleController = MockControllersProvider.toggleController(),
            featureItemsLoadingController = MockControllersProvider.loadingController(
                data = DashboardMockDataProvider.dashboardFeatureSectionsList(),
            ),
        )
    }
}