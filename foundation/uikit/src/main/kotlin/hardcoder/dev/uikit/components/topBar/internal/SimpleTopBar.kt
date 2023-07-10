package hardcoder.dev.uikit.components.topBar.internal

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.text.Text
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimpleTopBar(
    @StringRes titleResId: Int,
    actionConfig: ActionConfig? = null,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResId)) },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        actions = {
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
        },
    )
}

@UiKitPreview
@Composable
internal fun SimpleTopBarPreview() {
    HealtherThemePreview {
        SimpleTopBar(
            titleResId = R.string.placeholder_label,
            actionConfig = ActionConfig(
                actions = listOf(
                    Action(
                        iconResId = R.drawable.ic_fab_add,
                        onActionClick = {},
                    ),
                ),
            ),
        )
    }
}