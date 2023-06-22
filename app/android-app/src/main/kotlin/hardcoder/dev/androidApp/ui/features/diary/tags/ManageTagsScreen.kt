package hardcoder.dev.androidApp.ui.features.diary.tags

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
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag
import hardcoder.dev.presentation.features.diary.tags.TagViewModel
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.sections.EmptySection
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageTagsScreen(
    onGoBack: () -> Unit,
    onCreateTag: () -> Unit,
    onUpdateTag: (DiaryTag) -> Unit
) {
    val viewModel = koinViewModel<TagViewModel>()

    ScaffoldWrapper(
        content = {
            ManageTagsContent(
                tagsLoadingController = viewModel.diaryTagsLoadingController,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ManageTagsContent(
    tagsLoadingController: LoadingController<List<DiaryTag>>,
    onUpdateTag: (DiaryTag) -> Unit
) {
    LoadingContainer(
        controller = tagsLoadingController,
        loadedContent = { diaryTagList ->
            if (diaryTagList.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 4
                ) {
                    diaryTagList.forEach { tag ->
                        ActionChip(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { onUpdateTag(tag) },
                            text = tag.name,
                            iconResId = tag.icon.resourceId,
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
                    EmptySection(emptyTitleResId = R.string.diary_manageTags_nowEmpty_text)
                }
            }
        }
    )
}