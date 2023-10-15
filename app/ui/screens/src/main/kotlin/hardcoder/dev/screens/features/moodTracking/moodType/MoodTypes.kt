package hardcoder.dev.screens.features.moodTracking.moodType

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
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.icons.resourceId
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.features.MoodTrackingMockDataProvider
import hardcoder.dev.uikit.components.chip.Chip
import hardcoder.dev.uikit.components.chip.ChipConfig
import hardcoder.dev.uikit.components.container.FabConfig
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.section.EmptySection
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodTypes(
    moodTypesLoadingController: LoadingController<List<MoodType>>,
    onCreateMoodType: () -> Unit,
    onUpdateMoodType: (Int) -> Unit,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            MoodTypesContent(
                moodTypesLoadingController = moodTypesLoadingController,
                onUpdateMoodType = onUpdateMoodType,
            )
        },
        fabConfig = FabConfig(onFabClick = onCreateMoodType),
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

@HealtherScreenPhonePreviews
@Composable
private fun MoodTypesPreview() {
    HealtherTheme {
        MoodTypes(
            onGoBack = {},
            onCreateMoodType = {},
            onUpdateMoodType = {},
            moodTypesLoadingController = MockControllersProvider.loadingController(
                data = MoodTrackingMockDataProvider.moodTypesList(
                    context = LocalContext.current,
                ),
            ),
        )
    }
}