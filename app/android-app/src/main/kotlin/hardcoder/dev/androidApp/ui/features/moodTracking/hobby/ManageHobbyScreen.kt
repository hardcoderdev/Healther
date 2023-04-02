@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.features.moodTracking.hobby

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
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.entities.features.moodTracking.HobbyTrack
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.moodTracking.hobby.HobbyManageTracksViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun ManageHobbyScreen(
    onGoBack: () -> Unit,
    onCreateHobbyTrack: () -> Unit,
    onUpdateHobbyTrack: (HobbyTrack) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getHobbyManageTracksViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            ManageHobbyContent(
                state = state.value,
                onUpdateHobbyTrack = onUpdateHobbyTrack
            )
        },
        onFabClick = onCreateHobbyTrack,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_HobbyManageTracks_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun ManageHobbyContent(
    state: HobbyManageTracksViewModel.State,
    onUpdateHobbyTrack: (HobbyTrack) -> Unit
) {
    val uiModule = LocalUIModule.current
    val iconResolver = uiModule.iconResolver

    if (state.hobbyTrackList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            state.hobbyTrackList.forEach { hobbyTrack ->
                Chip(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onUpdateHobbyTrack(hobbyTrack) },
                    text = hobbyTrack.name,
                    interactionType = InteractionType.ACTION,
                    iconResId = iconResolver.toResourceId(hobbyTrack.iconResourceName),
                    shape = RoundedCornerShape(32.dp),
                    isSelected = state.hobbyTrackList.contains(hobbyTrack)
                )
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EmptySection(emptyTitleResId = R.string.moodTracking_HobbyManageTracks_nowEmpty_text)
        }
    }
}