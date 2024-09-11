package hardcoder.dev.screens.features.foodTracking.foodTracks.history

import androidx.compose.runtime.Composable
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FastingHistory(
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FastingHistoryContent()
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.history_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FastingHistoryContent() {

}