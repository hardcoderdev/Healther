package hardcoder.dev.screens.features.moodTracking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.fabButton.FabConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.topBar.Action
import hardcoder.dev.blocks.components.topBar.ActionConfig
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.moodTracking.MoodWithActivities
import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodTracking(
    dateTimeFormatter: DateTimeFormatter,
    moodWithActivitiesController: LoadingController<List<MoodWithActivities>>,
    onCreateMoodTrack: () -> Unit,
    onUpdateMoodTrack: (Int) -> Unit,
    onGoToHistory: () -> Unit,
    onGoToAnalytics: () -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTrackingContent(
                dateTimeFormatter = dateTimeFormatter,
                moodWithActivitiesController = moodWithActivitiesController,
                onUpdateMoodTrack = onUpdateMoodTrack,
            )
        },
        fabConfig = FabConfig.LargeFab(
            onFabClick = onCreateMoodTrack,
            iconResId = R.drawable.ic_create,
        ),
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
    dateTimeFormatter: DateTimeFormatter,
    moodWithActivitiesController: LoadingController<List<MoodWithActivities>>,
    onUpdateMoodTrack: (Int) -> Unit,
) {
    LoadingContainer(controller = moodWithActivitiesController) { moodWithActivitiesList ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (moodWithActivitiesList.isNotEmpty()) {
                MoodTrackingDiarySection(
                    dateTimeFormatter = dateTimeFormatter,
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
    dateTimeFormatter: DateTimeFormatter,
    moodWithActivitiesList: List<MoodWithActivities>,
    onUpdateTrack: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(2f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(moodWithActivitiesList) { moodWithActivityTrack ->
            MoodTrackItem(
                dateTimeFormatter = dateTimeFormatter,
                moodTrack = moodWithActivityTrack.moodTrack,
                activitiesList = moodWithActivityTrack.moodActivityList,
                onUpdate = {
                    onUpdateTrack(it.id)
                },
            )
        }
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodTrackingPreview() {
    HealtherTheme {
        MoodTracking(
            onGoToHistory = {},
            onGoToAnalytics = {},
            onCreateMoodTrack = {},
            onUpdateMoodTrack = {},
            onGoBack = {},
            dateTimeFormatter = DateTimeFormatter(LocalContext.current),
            moodWithActivitiesController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodWithActivitiesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}