package hardcoder.dev.androidApp.ui.screens.features.waterTracking.waterTrack

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
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.logic.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.WaterTrackingMockDataProvider
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.text.Headline
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun WaterTracking(
    waterTracksLoadingController: LoadingController<List<WaterTrackingItem>>,
    millilitersDrunkLoadingController: LoadingController<MillilitersDrunkToDailyRate>,
    progressController: LoadingController<Float>,
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
    onCreateWaterTrack: () -> Unit,
    onUpdateWaterTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoToAnalytics: () -> Unit,
    onGoBack: () -> Unit,
    isFabShowing: Boolean,
) {
    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                onUpdateWaterTrack = onUpdateWaterTrack,
                waterTracksLoadingController = waterTracksLoadingController,
                millilitersDrunkLoadingController = millilitersDrunkLoadingController,
                progressController = progressController,
                rewardLoadingController = rewardLoadingController,
                collectRewardController = collectRewardController,
            )
        },
        onFabClick = if (isFabShowing) onCreateWaterTrack else null,
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
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
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
            controller4 = rewardLoadingController,
        ) { waterTracks, millilitersDrunk, progress, totalReward ->
            DailyRateSection(millilitersDrunk, progress)
            if (totalReward != 0.0) {
                Spacer(modifier = Modifier.height(32.dp))
                RequestButtonWithIcon(
                    requestButtonConfig = RequestButtonConfig.Filled(
                        labelResId = R.string.currency_collectReward,
                        formatArgs = listOf(totalReward),
                        controller = collectRewardController,
                        iconResId = R.drawable.ic_money,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            if (waterTracks.isNotEmpty()) {
                TrackDiarySection(
                    waterTracks = waterTracks,
                    onUpdateWaterTrack = { waterTrack ->
                        if (waterTrack.isCollected) {
                            // TODO HANDLE CLICK ON ITEM WHEN TRACK ALREADY COLLECTED
                        } else {
                            onUpdateWaterTrack(waterTrack.id)
                        }
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
            isFabShowing = true,
            collectRewardController = MockControllersProvider.requestController(),
            rewardLoadingController = MockControllersProvider.loadingController(20.0),
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