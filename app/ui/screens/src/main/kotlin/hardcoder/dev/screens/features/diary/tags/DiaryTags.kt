package hardcoder.dev.screens.features.diary.tags

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
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.DiaryMockDataProvider
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun DiaryTags(
    diaryTagsLoadingController: LoadingController<List<DiaryTag>>,
    onCreateDiaryTag: () -> Unit,
    onUpdateDiaryTag: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            DiaryTagsContent(
                tagsLoadingController = diaryTagsLoadingController,
                onUpdateDiaryTag = onUpdateDiaryTag,
            )
        },
        onFabClick = onCreateDiaryTag,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.diary_tags_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DiaryTagsContent(
    tagsLoadingController: LoadingController<List<DiaryTag>>,
    onUpdateDiaryTag: (Int) -> Unit,
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
                    maxItemsInEachRow = 4,
                ) {
                    diaryTagList.forEach { tag ->
                        Chip(
                            chipConfig = ChipConfig.Action(
                                modifier = Modifier.padding(top = 8.dp),
                                onClick = { onUpdateDiaryTag(tag.id) },
                                text = tag.name,
                                iconResId = tag.icon.resourceId,
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
                    EmptySection(emptyTitleResId = R.string.diary_tags_nowEmpty_text)
                }
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun DiaryTagsPreview() {
    HealtherTheme {
        DiaryTags(
            onGoBack = {},
            onCreateDiaryTag = {},
            onUpdateDiaryTag = {},
            diaryTagsLoadingController = MockControllersProvider.loadingController(
                data = DiaryMockDataProvider.diaryTagsList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}