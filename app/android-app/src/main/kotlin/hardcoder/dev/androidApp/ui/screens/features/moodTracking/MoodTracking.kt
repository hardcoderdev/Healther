package hardcoder.dev.androidApp.ui.screens.features.moodTracking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivities
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun MoodTracking(
    viewModel: MoodTrackingViewModel,
    onCreateMoodTrack: () -> Unit,
    onUpdateMoodTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoToAnalytics: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        onFabClick = onCreateMoodTrack,
        content = {
            MoodTrackingContent(
                moodWithActivitiesController = viewModel.moodWithActivityLoadingController,
                rewardLoadingController = viewModel.rewardLoadingController,
                collectRewardController = viewModel.collectRewardController,
                onUpdateMoodTrack = onUpdateMoodTrack,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_title_topBar,
                onGoBack = onGoBack,
            ),
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
    )
}

@Composable
private fun MoodTrackingContent(
    moodWithActivitiesController: LoadingController<List<MoodWithActivities>>,
    rewardLoadingController: LoadingController<Double>,
    collectRewardController: RequestController,
    onUpdateMoodTrack: (Int) -> Unit,
) {
    LoadingContainer(
        controller1 = moodWithActivitiesController,
        controller2 = rewardLoadingController,
    ) { moodWithActivitiesList, totalReward ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            RequestButtonWithIcon(
                requestButtonConfig = RequestButtonConfig.Filled(
                    labelResId = R.string.waterTracking_collectReward,
                    formatArgs = listOf(totalReward),
                    controller = collectRewardController,
                    iconResId = R.drawable.ic_money,
                ),
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (moodWithActivitiesList.isNotEmpty()) {
                MoodTrackingDiarySection(
                    moodWithActivitiesList = moodWithActivitiesList,
                    onUpdateTrack = { moodTrackId ->
                        onUpdateMoodTrack(moodTrackId)
                    },
                )
            } else {
                EmptySection(emptyTitleResId = R.string.moodTracking_diaryNowEmpty_text)
            }
        }
    }
}

@Composable
private fun ColumnScope.MoodTrackingDiarySection(
    moodWithActivitiesList: List<MoodWithActivities>,
    onUpdateTrack: (Int) -> Unit,
) {
    Title(text = stringResource(id = R.string.moodTracking_diary_text))
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(moodWithActivitiesList) { moodWithActivityTrack ->
            MoodTrackItem(
                moodTrack = moodWithActivityTrack.moodTrack,
                activitiesList = moodWithActivityTrack.moodActivityList,
                onUpdate = {
                    if (moodWithActivityTrack.isRewardCollected) {
                        // TODO HANDLE CLICK ON ITEM WHEN TRACK ALREADY COLLECTED
                    } else {
                        onUpdateTrack(it.id)
                    }
                },
            )
        }
    }
}