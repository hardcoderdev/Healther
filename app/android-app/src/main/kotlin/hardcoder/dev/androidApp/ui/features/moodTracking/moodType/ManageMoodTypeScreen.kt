package hardcoder.dev.androidApp.ui.features.moodTracking.moodType

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
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.sections.EmptySection

@Composable
fun ManageMoodTypeScreen(
    onGoBack: () -> Unit,
    onCreateMoodType: () -> Unit,
    onUpdateMoodType: (MoodType) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getMoodTypeManageTracksViewModel() }

    ScaffoldWrapper(
        content = {
            ManageMoodTypeContent(
                moodTypesLoadingController = viewModel.moodTypesLoadingController,
                onUpdateMoodType = onUpdateMoodType
            )
        },
        onFabClick = onCreateMoodType,
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_moodTypeManageTracks_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ManageMoodTypeContent(
    moodTypesLoadingController: LoadingController<List<MoodType>>,
    onUpdateMoodType: (MoodType) -> Unit
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
                    maxItemsInEachRow = 4
                ) {
                    moodTypeList.forEach { moodType ->
                        ActionChip(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { onUpdateMoodType(moodType) },
                            text = moodType.name,
                            iconResId = moodType.icon.resourceId,
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
                    EmptySection(emptyTitleResId = R.string.moodTracking_moodTypeManageTracks_nowEmpty_text)
                }
            }
        }
    )
}