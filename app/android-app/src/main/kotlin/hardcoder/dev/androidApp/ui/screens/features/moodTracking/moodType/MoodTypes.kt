package hardcoder.dev.androidApp.ui.screens.features.moodTracking.moodType

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
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun MoodTypes(
    viewModel: MoodTypesViewModel,
    onCreateMoodType: () -> Unit,
    onUpdateMoodType: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTypesContent(
                moodTypesLoadingController = viewModel.moodTypesLoadingController,
                onUpdateMoodType = onUpdateMoodType,
            )
        },
        onFabClick = onCreateMoodType,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_moodTypes_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodTypesContent(
    moodTypesLoadingController: LoadingController<List<MoodType>>,
    onUpdateMoodType: (Int) -> Unit,
) {
    LoadingContainer(
        controller = moodTypesLoadingController,
        loadedContent = { moodTypeList ->
            if (moodTypeList.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 4,
                ) {
                    moodTypeList.forEach { moodType ->
                        Chip(
                            chipConfig = ChipConfig.Action(
                                modifier = Modifier.padding(top = 8.dp),
                                onClick = { onUpdateMoodType(moodType.id) },
                                text = moodType.name,
                                iconResId = moodType.icon.resourceId,
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
                    EmptySection(emptyTitleResId = R.string.moodTracking_moodTypes_nowEmpty_text)
                }
            }
        },
    )
}