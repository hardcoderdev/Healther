@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.moodTracking.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.presentation.features.moodTracking.activity.ManageActivitiesViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun ManageActivitiesScreen(
    onGoBack: () -> Unit,
    onCreateActivity: () -> Unit,
    onUpdateActivity: (Activity) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getManageActivitiesViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            ManageActivitiesContent(
                state = state.value,
                onUpdateActivity = onUpdateActivity
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

@Composable
private fun ManageActivitiesContent(
    state: ManageActivitiesViewModel.State,
    onUpdateActivity: (Activity) -> Unit
) {
    if (state.activityList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            state.activityList.forEach { activity ->
                Chip(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onUpdateActivity(activity) },
                    text = activity.name,
                    interactionType = InteractionType.ACTION,
                    iconResId = activity.icon.resourceId,
                    shape = RoundedCornerShape(32.dp),
                    isSelected = state.activityList.contains(activity)
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