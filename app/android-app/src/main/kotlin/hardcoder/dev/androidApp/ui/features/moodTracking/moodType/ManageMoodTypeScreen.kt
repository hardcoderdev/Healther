@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.moodTracking.moodType

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
import hardcoder.dev.logic.entities.features.moodTracking.MoodType
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeManageTracksViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun ManageMoodTypeScreen(
    onGoBack: () -> Unit,
    onCreateMoodType: () -> Unit,
    onUpdateMoodType: (MoodType) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getMoodTypeManageTracksViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            ManageMoodTypeContent(
                state = state.value,
                onUpdateMoodType = onUpdateMoodType
            )
        },
        onFabClick = onCreateMoodType,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_MoodTypeManageTracks_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun ManageMoodTypeContent(
    state: MoodTypeManageTracksViewModel.State,
    onUpdateMoodType: (MoodType) -> Unit
) {
    if (state.moodTypeList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            state.moodTypeList.forEach { moodType ->
                Chip(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onUpdateMoodType(moodType) },
                    text = moodType.name,
                    interactionType = InteractionType.ACTION,
                    iconResId = moodType.icon.resourceId,
                    shape = RoundedCornerShape(32.dp),
                    isSelected = state.moodTypeList.contains(moodType)
                )
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EmptySection(emptyTitleResId = R.string.moodTracking_MoodTypeManageTracks_nowEmpty_text)
        }
    }
}