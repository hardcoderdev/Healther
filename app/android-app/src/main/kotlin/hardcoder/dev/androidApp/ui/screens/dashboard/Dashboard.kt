package hardcoder.dev.androidApp.ui.screens.dashboard

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
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.DiaryFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.FastingFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.MoodTrackingFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.PedometerFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.WaterTrackingFeatureItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.presentation.dashboard.DashboardItem
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    onGoToDiary: () -> Unit,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onGoToSettings: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DashboardContent(
                onGoToDiary = onGoToDiary,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                itemsLoadingController = viewModel.itemsLoadingController,
                pedometerToggleController = viewModel.pedometerToggleController,
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
    onGoToDiary: () -> Unit,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    itemsLoadingController: LoadingController<List<DashboardItem>>,
    pedometerToggleController: ToggleController,
) {
    LoadingContainer(controller = itemsLoadingController) { items ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        ) {
            // TODO USER CHARACTER SECTION WILL BE HERE
            featureSection(
                items = items,
                onGoToDiary = onGoToDiary,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onTogglePedometerTrackingService = pedometerToggleController::toggle,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
            )
        }
    }
}

private fun LazyListScope.featureSection(
    items: List<DashboardItem>,
    onGoToDiary: () -> Unit,
    onGoToWaterTrackingFeature: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
) {
    items(items) { feature ->
        when (feature) {
            is DashboardItem.DiaryFeature -> {
                DiaryFeatureItem(
                    diaryFeature = feature,
                    onGoToFeature = onGoToDiary,
                )
            }

            is DashboardItem.FastingFeature -> {
                FastingFeatureItem(
                    fastingFeature = feature,
                    onGoToFeature = onGoToFastingFeature,
                    onStartFasting = onStartFasting,
                )
            }

            is DashboardItem.MoodTrackingFeature -> {
                MoodTrackingFeatureItem(
                    moodTrackingFeature = feature,
                    onGoToFeature = onGoToMoodTrackingFeature,
                )
            }

            is DashboardItem.PedometerFeature -> {
                PedometerFeatureItem(
                    pedometerFeature = feature,
                    onGoToFeature = onGoToPedometerFeature,
                    onTogglePedometerTrackingService = onTogglePedometerTrackingService,
                )
            }

            is DashboardItem.WaterTrackingFeature -> {
                WaterTrackingFeatureItem(
                    waterTrackingFeature = feature,
                    onGoToFeature = onGoToWaterTrackingFeature,
                )
            }
        }
    }
}