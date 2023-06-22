package hardcoder.dev.androidApp.ui.features.moodTracking.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.activity.MoodActivity
import hardcoder.dev.presentation.features.moodTracking.activity.ActivityViewModel
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.sections.EmptySection
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageMoodActivities(
    onGoBack: () -> Unit,
    onCreateMoodActivity: () -> Unit,
    onUpdateMoodActivity: (Int) -> Unit
) {
    val viewModel = koinViewModel<ActivityViewModel>()

    ScaffoldWrapper(
        content = {
            ManageMoodActivitiesContent(
                onUpdateActivity = onUpdateMoodActivity,
                activitiesLoadingController = viewModel.activitiesLoadingController
            )
        },
        onFabClick = onCreateMoodActivity,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_manageActivities_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ManageMoodActivitiesContent(
    activitiesLoadingController: LoadingController<List<MoodActivity>>,
    onUpdateActivity: (Int) -> Unit
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
                    maxItemsInEachRow = 4
                ) {
                    activitiesList.forEach { activity ->
                        ActionChip(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { onUpdateActivity(activity.id) },
                            text = activity.name,
                            iconResId = activity.icon.resourceId,
                            shape = RoundedCornerShape(32.dp)
                        )
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    EmptySection(emptyTitleResId = R.string.moodTracking_manageActivities_nowEmpty_text)
                }
            }
        }
    )
}