package hardcoder.dev.uikit.components.list.tabRow

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    indicator: @Composable (List<TabPosition>) -> Unit = {},
    divider: @Composable () -> Unit = {},
    edgePadding: Dp = 0.dp,
    tabRowContent: @Composable () -> Unit,
) {
    androidx.compose.material3.ScrollableTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        indicator = indicator,
        divider = divider,
        edgePadding = edgePadding,
    ) {
        tabRowContent()
    }
}