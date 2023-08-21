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
import hardcoder.dev.androidApp.ui.screens.dashboard.heroItems.HeroSectionItem
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.presentation.dashboard.DashboardHeroItem
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
    onGoToWaterTrackingFeature: () -> Unit,
    onCreateWaterTrack: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onGoToDiary: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToDeathScreen: () -> Unit,
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DashboardContent(
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onCreateWaterTrack = onCreateWaterTrack,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                onCreateMoodTrack = onCreateMoodTrack,
                onGoToDiary = onGoToDiary,
                onCreateDiaryTrack = onCreateDiaryTrack,
                onGoToDeathScreen = onGoToDeathScreen,
                onGoToInventory = onGoToInventory,
                onGoToShop = onGoToShop,
                featureItemsLoadingController = viewModel.featuresLoadingController,
                heroItemsLoadingController = viewModel.heroLoadingController,
                pedometerToggleController = viewModel.pedometerToggleController,
                healthPointsLoadingController = viewModel.healthPointsLoadingController,
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
    onGoToPedometerFeature: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
    onGoToDiary: () -> Unit,
    onCreateDiaryTrack: () -> Unit,
    onGoToDeathScreen: () -> Unit,
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
    featureItemsLoadingController: LoadingController<List<DashboardFeatureItem>>,
    heroItemsLoadingController: LoadingController<DashboardHeroItem.HeroSection>,
    pedometerToggleController: ToggleController,
    healthPointsLoadingController: LoadingController<Int>,
) {
    LoadingContainer(
        controller1 = featureItemsLoadingController,
        controller2 = heroItemsLoadingController,
        controller3 = healthPointsLoadingController,
    ) { featureItems, heroItems, healthPoints ->
        // TODO А ЭТО ТОЧНО ЗДЕСЬ НУЖНО? ОНО ЖЕ ЕСТ НА СПЛЕШЕ
        if (healthPoints <= 0) {
            onGoToDeathScreen()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        ) {
            heroCharacterSection(
                item = heroItems,
                onGoToInventory = onGoToInventory,
                onGoToShop = onGoToShop,
            )
            featureSection(
                items = featureItems,
                onGoToDiary = onGoToDiary,
                onGoToWaterTrackingFeature = onGoToWaterTrackingFeature,
                onCreateWaterTrack = onCreateWaterTrack,
                onGoToPedometerFeature = onGoToPedometerFeature,
                onTogglePedometerTrackingService = pedometerToggleController::toggle,
                onGoToFastingFeature = onGoToFastingFeature,
                onStartFasting = onStartFasting,
                onGoToMoodTrackingFeature = onGoToMoodTrackingFeature,
                onCreateMoodTrack = onCreateMoodTrack,
                onCreateDiaryTrack = onCreateDiaryTrack,
            )
        }
    }
}

private fun LazyListScope.heroCharacterSection(
    item: DashboardHeroItem.HeroSection,
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
) {
    item {
        HeroSectionItem(
            hero = item.hero,
            healthPointsProgress = item.healthPointsProgress,
            experiencePointsProgress = item.experiencePointsProgress,
            experiencePointsToNextLevel = item.experiencePointsToNextLevel,
            onGoToInventory = onGoToInventory,
            onGoToShop = onGoToShop,
        )
    }
}

private fun LazyListScope.featureSection(
    items: List<DashboardFeatureItem>,
    onGoToWaterTrackingFeature: () -> Unit,
    onCreateWaterTrack: () -> Unit,
    onGoToPedometerFeature: () -> Unit,
    onTogglePedometerTrackingService: () -> Unit,
    onGoToFastingFeature: () -> Unit,
    onStartFasting: () -> Unit,
    onGoToMoodTrackingFeature: () -> Unit,
    onCreateMoodTrack: () -> Unit,
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

            is DashboardFeatureItem.PedometerFeature -> {
                PedometerFeatureItem(
                    pedometerFeature = feature,
                    onGoToFeature = onGoToPedometerFeature,
                    onTogglePedometerTrackingService = onTogglePedometerTrackingService,
                )
            }

            is DashboardFeatureItem.FastingFeature -> {
                FastingFeatureItem(
                    fastingFeature = feature,
                    onGoToFeature = onGoToFastingFeature,
                    onStartFasting = onStartFasting,
                )
            }

            is DashboardFeatureItem.MoodTrackingFeature -> {
                MoodTrackingFeatureItem(
                    moodTrackingFeature = feature,
                    onGoToFeature = onGoToMoodTrackingFeature,
                    onCreateMoodTrack = onCreateMoodTrack,
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