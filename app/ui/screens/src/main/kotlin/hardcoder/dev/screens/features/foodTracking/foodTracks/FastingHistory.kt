package hardcoder.dev.screens.features.foodTracking.foodTracks

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
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