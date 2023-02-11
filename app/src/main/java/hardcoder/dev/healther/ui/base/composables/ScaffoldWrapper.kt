@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.base.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import hardcoder.dev.healther.R

@Composable
fun ScaffoldWrapper(
    @StringRes titleResId: Int,
    content: @Composable () -> Unit,
    onGoBack: (() -> Unit)? = null,
    onFabClick: (() -> Unit)? = null,
    actionConfig: ActionConfig? = null
) {
    Scaffold(
        topBar = {
            onGoBack?.let {
                GoBackTopBar(
                    titleResId = titleResId,
                    onGoBack = onGoBack,
                    actionConfig = actionConfig
                )
            } ?: run {
                SimpleTopBar(titleResId = titleResId)
            }
        },
        floatingActionButton = {
            onFabClick?.let {
                LargeFloatingActionButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_water_track_cd)
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
        ),
    )
}

data class Action(val iconImageVector: ImageVector, val onActionClick: () -> Unit)

data class ActionConfig(val actions: List<Action>)

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
            IconButton(onClick = onGoBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.go_back_cd)
                )
            }
        },
        actions = {
            actionConfig?.let {
                it.actions.forEach { action ->
                    IconButton(onClick = action.onActionClick) {
                        Icon(
                            imageVector = action.iconImageVector,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    )
}