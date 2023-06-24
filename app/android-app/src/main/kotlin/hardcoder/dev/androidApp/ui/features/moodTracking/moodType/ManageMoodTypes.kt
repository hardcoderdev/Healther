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
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.logic.features.moodTracking.moodType.MoodType
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import hardcoder.dev.uikit.LoadingContainer
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.chip.ActionChip
import hardcoder.dev.uikit.sections.EmptySection
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageMoodTypes(
    onGoBack: () -> Unit,
    onCreateMoodType: () -> Unit,
    onUpdateMoodType: (MoodType) -> Unit
) {
    val viewModel = koinViewModel<MoodTypesViewModel>()

    ScaffoldWrapper(
        content = {
            ManageMoodTypesContent(
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
private fun ManageMoodTypesContent(
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