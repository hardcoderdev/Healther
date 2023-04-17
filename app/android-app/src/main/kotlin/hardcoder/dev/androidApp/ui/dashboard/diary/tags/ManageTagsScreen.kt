@file:OptIn(ExperimentalLayoutApi::class)

package hardcoder.dev.androidApp.ui.dashboard.diary.tags

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
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag
import hardcoder.dev.presentation.dashboard.features.diary.tags.ManageTagsViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.Chip
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun ManageTagsScreen(
    onGoBack: () -> Unit,
    onCreateTag: () -> Unit,
    onUpdateTag: (DiaryTag) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getManageTagsViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            ManageTagsContent(
                state = state.value,
                onUpdateTag = onUpdateTag
            )
        },
        onFabClick = onCreateTag,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_manageTags_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun ManageTagsContent(
    state: ManageTagsViewModel.State,
    onUpdateTag: (DiaryTag) -> Unit
) {
    if (state.diaryTagList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            state.diaryTagList.forEach { tag ->
                Chip(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onUpdateTag(tag) },
                    text = tag.name,
                    interactionType = InteractionType.ACTION,
                    iconResId = tag.icon.resourceId,
                    shape = RoundedCornerShape(32.dp),
                    isSelected = state.diaryTagList.contains(tag)
                )
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EmptySection(emptyTitleResId = R.string.diary_manageTags_nowEmpty_text)
        }
    }
}