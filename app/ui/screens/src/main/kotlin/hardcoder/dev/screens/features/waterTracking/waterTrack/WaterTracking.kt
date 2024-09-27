package hardcoder.dev.screens.features.waterTracking.waterTrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.fabButton.FabConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.progressBar.LinearProgressBar
import hardcoder.dev.blocks.components.text.Headline
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.Action
import hardcoder.dev.blocks.components.topBar.ActionConfig
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun WaterTracking(
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    millilitersDrunkLoadingController: LoadingController<MillilitersDrunkToDailyRate>,
    progressController: LoadingController<Float>,
    onCreateWaterTrack: () -> Unit,
    onUpdateWaterTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoToAnalytics: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                onUpdateWaterTrack = onUpdateWaterTrack,
                waterTracksLoadingController = waterTracksLoadingController,
                millilitersDrunkLoadingController = millilitersDrunkLoadingController,
                progressController = progressController,
            )
        },
        fabConfig = FabConfig.LargeFab(
            iconResId = R.drawable.ic_create,
            onFabClick = onCreateWaterTrack
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_history,
                    onActionClick = onGoToHistory,
                ),
                Action(
                    iconResId = R.drawable.ic_analytics,
                    onActionClick = onGoToAnalytics,
                ),
            ),
        ),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun WaterTrackingContent(
    onUpdateWaterTrack: (Int) -> Unit,
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    millilitersDrunkLoadingController: LoadingController<MillilitersDrunkToDailyRate>,
    progressController: LoadingController<Float>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        LoadingContainer(
            controller1 = waterTracksLoadingController,
            controller2 = millilitersDrunkLoadingController,
            controller3 = progressController,
        ) { waterTracks, millilitersDrunk, progress ->
            DailyRateSection(millilitersDrunk, progress)
            Spacer(modifier = Modifier.height(32.dp))
            if (waterTracks.isNotEmpty()) {
                TrackDiarySection(
                    waterTracks = waterTracks,
                    onUpdateWaterTrack = { waterTrack ->
                        onUpdateWaterTrack(waterTrack.id)
                    },
                )
            } else {
                EmptySection(emptyTitleResId = R.string.waterTracking_nowEmpty_text)
            }
        }
    }
}

@Composable
private fun DailyRateSection(
    millilitersDrunk: MillilitersDrunkToDailyRate,
    progress: Float,
) {
    Headline(
        text = stringResource(
            id = R.string.waterTracking_millilitersCount_formatText,
            formatArgs = arrayOf(
                millilitersDrunk.millilitersDrunkCount,
                millilitersDrunk.dailyWaterIntake,
            ),
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    LinearProgressBar(indicatorProgress = progress)
}

@Composable
private fun ColumnScope.TrackDiarySection(
    waterTracks: List<WaterTrackingItem>,
    onUpdateWaterTrack: (WaterTrackingItem) -> Unit,
) {
    Title(text = stringResource(id = R.string.waterTracking_diary_text))
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(waterTracks) { track ->
            WaterTrackItem(
                waterTrackingItem = track,
                onUpdate = onUpdateWaterTrack,
            )
        }
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun WaterTrackingPreview() {
    HealtherTheme {
        WaterTracking(
            onGoBack = {},
            onGoToAnalytics = {},
            onGoToHistory = {},
            onCreateWaterTrack = {},
            onUpdateWaterTrack = {},
            progressController = MockControllersProvider.loadingController(0.7f),
            millilitersDrunkLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.millilitersDrunkToDailyRate(),
            ),
            waterTracksLoadingController = MockControllersProvider.loadingController(
                data = WaterTrackingMockDataProvider.waterTrackingItemsList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}