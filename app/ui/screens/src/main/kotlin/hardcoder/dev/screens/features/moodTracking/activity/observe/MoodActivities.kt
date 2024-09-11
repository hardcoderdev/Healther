package hardcoder.dev.screens.features.moodTracking.activity.observe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hardcoder.dev.blocks.components.button.fabButton.FabConfig
import hardcoder.dev.blocks.components.chip.Chip
import hardcoder.dev.blocks.components.chip.ChipConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.moodTracking.MoodActivity
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.sections.EmptySection
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodActivities(
    activitiesLoadingController: LoadingController<List<MoodActivity>>,
    onCreateMoodActivity: () -> Unit,
    onUpdateMoodActivity: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodActivitiesContent(
                onUpdateActivity = onUpdateMoodActivity,
                activitiesLoadingController = activitiesLoadingController,
            )
        },
        fabConfig = FabConfig.LargeFab(
            onFabClick = onCreateMoodActivity,
            iconResId = R.drawable.ic_create,
        ),
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_activities_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodActivitiesContent(
    activitiesLoadingController: LoadingController<List<MoodActivity>>,
    onUpdateActivity: (Int) -> Unit,
) {
    LoadingContainer(
        controller = activitiesLoadingController,
        loadedContent = { activitiesList ->
            if (activitiesList.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 4,
                ) {
                    activitiesList.forEach { activity ->
                        Chip(
                            chipConfig = ChipConfig.Action(
                                modifier = Modifier.padding(top = 8.dp),
                                onClick = { onUpdateActivity(activity.id) },
                                text = activity.name,
                                iconResId = activity.icon.resourceId,
                                shape = RoundedCornerShape(32.dp),
                            ),
                        )
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    EmptySection(emptyTitleResId = R.string.moodTracking_activities_nowEmpty_text)
                }
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun MoodActivitiesPreview() {
    HealtherTheme {
        MoodActivities(
            onGoBack = {},
            onCreateMoodActivity = {},
            onUpdateMoodActivity = {},
            activitiesLoadingController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodActivitiesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}