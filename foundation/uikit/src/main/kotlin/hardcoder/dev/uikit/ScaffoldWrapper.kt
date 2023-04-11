@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)

package hardcoder.dev.uikit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.icons.IconButton
import kotlinx.coroutines.channels.ticker

@Composable
fun ScaffoldWrapper(
    content: @Composable () -> Unit,
    onFabClick: (() -> Unit)? = null,
    topBarConfig: TopBarConfig,
    dropdownConfig: DropdownConfig? = null,
    actionConfig: ActionConfig? = null
) {
    Scaffold(
        topBar = {
            when (topBarConfig.type) {
                is TopBarType.TitleTopBar -> SimpleTopBar(titleResId = topBarConfig.type.titleResId)
                is TopBarType.TopBarWithNavigationBack -> GoBackTopBar(
                    titleResId = topBarConfig.type.titleResId,
                    onGoBack = topBarConfig.type.onGoBack,
                    dropdownConfig = dropdownConfig,
                    actionConfig = actionConfig
                )
                is TopBarType.SearchTopBar -> SearchTopBar(
                    titleResId = topBarConfig.type.titleResId,
                    searchText = topBarConfig.type.searchText,
                    placeholderText = topBarConfig.type.placeholderText,
                    actionConfig = actionConfig,
                    onGoBack = topBarConfig.type.onGoBack,
                    onSearchTextChanged = topBarConfig.type.onSearchTextChanged,
                    onClearClick = topBarConfig.type.onClearClick
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
private fun SimpleTopBar(@StringRes titleResId: Int) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResId)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

data class DropdownItem(
    val name: String,
    val onDropdownItemClick: () -> Unit
)

data class DropdownConfig(val actionToggle: Action, val dropdownItems: List<DropdownItem>)

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

    data class SearchTopBar(
        @StringRes val titleResId: Int,
        val searchText: String,
        @StringRes val placeholderText: Int,
        val onGoBack: () -> Unit,
        val onSearchTextChanged: (String) -> Unit,
        val onClearClick: () -> Unit
    ) : TopBarType()
}

@Composable
private fun GoBackTopBar(
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    dropdownConfig: DropdownConfig? = null,
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
            val isExpanded = remember {
                mutableStateOf(false)
            }

            actionConfig?.let {
                it.actions.forEach { action ->
                    IconButton(onClick = action.onActionClick, iconResId = action.iconResId)
                }
            }

            dropdownConfig?.let {
                IconButton(
                    iconResId = it.actionToggle.iconResId,
                    contentDescription = null,
                    onClick = { isExpanded.value = !isExpanded.value }
                )

                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = {
                        isExpanded.value = false
                    }
                ) {
                    it.dropdownItems.forEach { dropdownItem ->
                        DropdownMenuItem(
                            text = { Text(dropdownItem.name) },
                            onClick = {
                                dropdownItem.onDropdownItemClick()
                                isExpanded.value = false
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SearchTopBar(
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    actionConfig: ActionConfig? = null,
    searchText: String,
    placeholderText: Int,
    onSearchTextChanged: (String) -> Unit,
    onClearClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isSearchModeEnabled by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = !isSearchModeEnabled,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = stringResource(id = titleResId))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            AnimatedVisibility(
                visible = !isSearchModeEnabled,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + shrinkHorizontally { -it }
            ) {
                IconButton(onClick = onGoBack, iconResId = R.drawable.ic_top_bar_back)
            }
        },
        actions = {
            AnimatedVisibility(
                visible = !isSearchModeEnabled,
                enter = fadeIn(tween(delayMillis = 250)) + scaleIn(tween(delayMillis = 250)),
                exit = fadeOut(tween(1)) + shrinkHorizontally { -it }
            ) {
                Row {
                    IconButton(
                        onClick = { isSearchModeEnabled = true },
                        iconResId = R.drawable.ic_search
                    )
                    actionConfig?.let {
                        it.actions.forEach { action ->
                            IconButton(onClick = action.onActionClick, iconResId = action.iconResId)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isSearchModeEnabled,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .heightIn(min = 40.dp, max = 60.dp)
                        .padding(horizontal = 16.dp)
                        .focusRequester(focusRequester),
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    textStyle = MaterialTheme.typography.labelLarge,
                    placeholder = {
                        Text(
                            text = stringResource(id = placeholderText),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    ),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = isSearchModeEnabled,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Icon(
                                iconResId = R.drawable.ic_clear,
                                contentDescription = stringResource(id = R.string.icon_search_clear_content_description),
                                modifier = Modifier.clickable {
                                    onClearClick()
                                    inputService?.hide()
                                    focusManager.clearFocus()
                                    isSearchModeEnabled = false
                                }
                            )
                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    })
                )

                LaunchedEffect(isSearchModeEnabled) {
                    if (isSearchModeEnabled) {
                        focusRequester.requestFocus()
                        inputService?.show()
                    }
                }
            }
        }
    )
}