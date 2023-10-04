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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.DiaryFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.FastingFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.MoodTrackingFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.PedometerFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.featureItems.WaterTrackingFeatureItem
import hardcoder.dev.androidApp.ui.screens.dashboard.heroItems.HeroSectionItem
import hardcoder.dev.androidApp.ui.screens.hero.HeroImageByGenderResolver
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.DashboardMockDataProvider
import hardcoder.dev.presentation.dashboard.DashboardFeatureItem
import hardcoder.dev.presentation.dashboard.DashboardHeroItem
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun Dashboard(
    millisDistanceFormatter: MillisDistanceFormatter,
    heroImageByGenderResolver: HeroImageByGenderResolver,
    decimalFormatter: DecimalFormatter,
    featureItemsLoadingController: LoadingController<List<DashboardFeatureItem>>,
    heroItemsLoadingController: LoadingController<DashboardHeroItem.HeroSection>,
    pedometerToggleController: ToggleController,
    healthPointsLoadingController: LoadingController<Int>,
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
                millisDistanceFormatter = millisDistanceFormatter,
                heroImageByGenderResolver = heroImageByGenderResolver,
                decimalFormatter = decimalFormatter,
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
                featureItemsLoadingController = featureItemsLoadingController,
                heroItemsLoadingController = heroItemsLoadingController,
                pedometerToggleController = pedometerToggleController,
                healthPointsLoadingController = healthPointsLoadingController,
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
    millisDistanceFormatter: MillisDistanceFormatter,
    heroImageByGenderResolver: HeroImageByGenderResolver,
    decimalFormatter: DecimalFormatter,
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
                heroImageByGenderResolver = heroImageByGenderResolver,
                decimalFormatter = decimalFormatter,
                item = heroItems,
                onGoToInventory = onGoToInventory,
                onGoToShop = onGoToShop,
            )
            featureSection(
                millisDistanceFormatter = millisDistanceFormatter,
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
    heroImageByGenderResolver: HeroImageByGenderResolver,
    decimalFormatter: DecimalFormatter,
    item: DashboardHeroItem.HeroSection,
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
) {
    item {
        HeroSectionItem(
            heroImageByGenderResolver = heroImageByGenderResolver,
            decimalFormatter = decimalFormatter,
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
    millisDistanceFormatter: MillisDistanceFormatter,
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
                    millisDistanceFormatter = millisDistanceFormatter,
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

@HealtherScreenPhonePreviews
@Composable
private fun DashboardPreview() {
    HealtherTheme {
        Dashboard(
            onCreateDiaryTrack = {},
            onCreateMoodTrack = {},
            onCreateWaterTrack = {},
            onGoToDeathScreen = {},
            onGoToDiary = {},
            onGoToFastingFeature = {},
            onGoToInventory = {},
            onGoToMoodTrackingFeature = {},
            onGoToPedometerFeature = {},
            onGoToSettings = {},
            onGoToShop = {},
            onGoToWaterTrackingFeature = {},
            onStartFasting = {},
            heroImageByGenderResolver = HeroImageByGenderResolver(),
            decimalFormatter = DecimalFormatter(),
            millisDistanceFormatter = MillisDistanceFormatter(
                context = LocalContext.current,
                defaultAccuracy = MillisDistanceFormatter.Accuracy.DAYS,
            ),
            pedometerToggleController = MockControllersProvider.toggleController(),
            healthPointsLoadingController = MockControllersProvider.loadingController(50),
            heroItemsLoadingController = MockControllersProvider.loadingController(
                data = DashboardMockDataProvider.dashboardHeroSection(),
            ),
            featureItemsLoadingController = MockControllersProvider.loadingController(
                data = DashboardMockDataProvider.dashboardFeatureSectionsList(),
            ),
        )
    }
}