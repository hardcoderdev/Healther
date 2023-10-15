package hardcoder.dev.uikit.components.container

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.tooltip.Tooltip
import hardcoder.dev.uikit.components.tooltip.TooltipConfig
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.DropdownConfig
import hardcoder.dev.uikit.components.topBar.DropdownItem
import hardcoder.dev.uikit.components.topBar.TopBar
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoderdev.healther.foundation.uikit.R

data class FabConfig(
    val modifier: Modifier = Modifier,
    @DrawableRes val iconResId: Int = R.drawable.ic_add,
    @StringRes val tooltipResId: Int = R.string.tooltip_fab_creation,
    val onFabClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWrapper(
    content: @Composable () -> Unit,
    topBarConfig: TopBarConfig,
    fabConfig: FabConfig? = null,
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
            fabConfig?.let {
                Tooltip(
                    tooltipConfig = TooltipConfig.Plain(
                        tooltipResId = fabConfig.tooltipResId,
                        modifier = Modifier.padding(end = 16.dp, bottom = 8.dp),
                        content = {
                            LargeFloatingActionButton(
                                modifier = Modifier.tooltipAnchor(),
                                onClick = fabConfig.onFabClick,
                            ) {
                                Icon(
                                    iconResId = fabConfig.iconResId,
                                    contentDescription = null,
                                )
                            }
                        },
                    ),
                )
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

@HealtherUiKitPreview
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
                    iconResId = R.drawable.ic_add,
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
        fabConfig = FabConfig(
            tooltipResId = R.string.default_nowEmpty_text,
            onFabClick = {},
        ),
    )
}