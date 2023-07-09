package hardcoder.dev.uikit.components.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.DropdownConfig
import hardcoder.dev.uikit.components.topBar.DropdownItem
import hardcoder.dev.uikit.components.topBar.TopBar
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun ScaffoldWrapper(
    content: @Composable () -> Unit,
    onFabClick: (() -> Unit)? = null,
    topBarConfig: TopBarConfig,
    dropdownConfig: DropdownConfig? = null,
    actionConfig: ActionConfig? = null,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TopBar(
                topBarConfig = topBarConfig,
                actionConfig = actionConfig,
                dropdownConfig = dropdownConfig,
            )
        },
        floatingActionButton = {
            onFabClick?.let {
                LargeFloatingActionButton(onClick = it) {
                    Icon(
                        iconResId = R.drawable.ic_fab_add,
                        contentDescription = null,
                    )
                }
            } ?: Unit
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            content()
        }
    }
}

@UiKitPreview
@Composable
private fun ScaffoldWrapperPreview() {
    ScaffoldWrapper(
        content = { },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.placeholder_label,
                onGoBack = {},
            ),
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_fab_add,
                    onActionClick = {},
                ),
            ),
        ),
        dropdownConfig = DropdownConfig(
            actionToggle = Action(
                iconResId = R.drawable.ic_drop_down,
                onActionClick = {},
            ),
            dropdownItems = listOf(
                DropdownItem(
                    name = "Add",
                    onDropdownItemClick = {},
                ),
                DropdownItem(
                    name = "Remove",
                    onDropdownItemClick = {},
                ),
                DropdownItem(
                    name = "Clear",
                    onDropdownItemClick = {},
                ),
            ),
        ),
        onFabClick = {
            // some logic
        },
    )
}