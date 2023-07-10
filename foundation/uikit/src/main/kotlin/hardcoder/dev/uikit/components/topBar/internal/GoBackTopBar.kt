package hardcoder.dev.uikit.components.topBar.internal

import androidx.annotation.StringRes
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.text.Text
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.components.topBar.DropdownConfig
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoBackTopBar(
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    dropdownConfig: DropdownConfig? = null,
    actionConfig: ActionConfig? = null,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResId)) },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            CircleIconButton(
                circleIconButtonConfig = CircleIconButtonConfig.Filled(
                    onClick = onGoBack,
                    iconResId = R.drawable.ic_top_bar_back,
                ),
            )
        },
        actions = {
            val isExpanded = remember {
                mutableStateOf(false)
            }

            actionConfig?.let {
                it.actions.forEach { action ->
                    CircleIconButton(
                        circleIconButtonConfig = CircleIconButtonConfig.Filled(
                            onClick = action.onActionClick,
                            iconResId = action.iconResId,
                        ),
                    )
                }
            }

            dropdownConfig?.let {
                CircleIconButton(
                    circleIconButtonConfig = CircleIconButtonConfig.Filled(
                        onClick = { isExpanded.value = !isExpanded.value },
                        iconResId = it.actionToggle.iconResId,
                        contentDescription = null,
                    ),
                )

                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = {
                        isExpanded.value = false
                    },
                ) {
                    it.dropdownItems.forEach { dropdownItem ->
                        DropdownMenuItem(
                            text = { Text(dropdownItem.name) },
                            onClick = {
                                dropdownItem.onDropdownItemClick()
                                isExpanded.value = false
                            },
                        )
                    }
                }
            }
        },
    )
}

@UiKitPreview
@Composable
internal fun GoBackTopBarPreview() {
    HealtherThemePreview {
        GoBackTopBar(
            titleResId = R.string.default_nowEmpty_text,
            onGoBack = { /* no-op */ },
        )
    }
}