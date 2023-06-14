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
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.sections.EmptySection
import hardcoderdev.healther.app.android.app.R

@Composable
fun ManageActivitiesScreen(
    onGoBack: () -> Unit,
    onCreateActivity: () -> Unit,
    onUpdateActivity: (Activity) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getManageActivitiesViewModel() }

    ScaffoldWrapper(
        content = {
            ManageActivitiesContent(
                onUpdateActivity = onUpdateActivity,
                activitiesLoadingController = viewModel.activitiesLoadingController
            )
        },
        onFabClick = onCreateActivity,
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
private fun ManageActivitiesContent(
    activitiesLoadingController: LoadingController<List<Activity>>,
    onUpdateActivity: (Activity) -> Unit
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
                            onClick = { onUpdateActivity(activity) },
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