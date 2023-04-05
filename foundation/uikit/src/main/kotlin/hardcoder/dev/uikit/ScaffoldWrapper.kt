@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.icons.IconButton

@Composable
fun ScaffoldWrapper(
    content: @Composable () -> Unit,
    onFabClick: (() -> Unit)? = null,
    topBarConfig: TopBarConfig,
    actionConfig: ActionConfig? = null
) {
    Scaffold(
        topBar = {
            when (topBarConfig.type) {
                is TopBarType.TitleTopBar -> SimpleTopBar(titleResId = topBarConfig.type.titleResId)
                is TopBarType.TopBarWithNavigationBack -> GoBackTopBar(
                    titleResId = topBarConfig.type.titleResId,
                    onGoBack = topBarConfig.type.onGoBack,
                    actionConfig = actionConfig
                )

                is TopBarType.WithoutTopBar -> null
            }
        },
        floatingActionButton = {
            onFabClick?.let {
                LargeFloatingActionButton(onClick = it) {
                    Icon(
                        iconResId = R.drawable.ic_fab_add,
                        contentDescription = null
                    )
                }
            } ?: Unit
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            content()
        }
    }
}

@Composable
fun SimpleTopBar(@StringRes titleResId: Int) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResId)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

data class Action(@DrawableRes val iconResId: Int, val onActionClick: () -> Unit)

data class ActionConfig(val actions: List<Action>)

data class TopBarConfig(val type: TopBarType)

sealed class TopBarType {
    object WithoutTopBar : TopBarType()
    data class TitleTopBar(@StringRes val titleResId: Int) : TopBarType()
    data class TopBarWithNavigationBack(
        @StringRes val titleResId: Int,
        val onGoBack: () -> Unit
    ) : TopBarType()
}

@Composable
fun GoBackTopBar(
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    actionConfig: ActionConfig? = null,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResId)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onGoBack, iconResId = R.drawable.ic_top_bar_back)
        },
        actions = {
            actionConfig?.let {
                it.actions.forEach { action ->
                    IconButton(onClick = action.onActionClick, iconResId = action.iconResId)
                }
            }
        }
    )
}