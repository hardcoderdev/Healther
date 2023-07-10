@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit.components.topBar.internal

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButton
import hardcoder.dev.uikit.components.button.circleIconButton.CircleIconButtonConfig
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.topBar.Action
import hardcoder.dev.uikit.components.topBar.ActionConfig
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun SearchTopBar(
    controller: InputController<String>,
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    actionConfig: ActionConfig? = null,
    placeholderText: Int,
) {
    var isSearchModeEnabled by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            TopBarTitle(
                titleResId = titleResId,
                placeholderText = placeholderText,
                searchText = controller.getInput(),
                onSearchTextChanged = controller::changeInput,
                isSearchModeEnabled = isSearchModeEnabled,
                onToggleSearchMode = { isSearchModeEnabled = it },
                onClearClick = {
                    controller.changeInput("")
                },
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            AnimatedVisibility(
                visible = !isSearchModeEnabled,
                enter = fadeIn() + expandHorizontally { -it },
                exit = fadeOut() + shrinkHorizontally { -it },
            ) {
                CircleIconButton(
                    circleIconButtonConfig = CircleIconButtonConfig.Filled(
                        onClick = onGoBack,
                        iconResId = R.drawable.ic_top_bar_back,
                    ),
                )
            }
        },
        actions = {
            TopBarActions(
                actionConfig = actionConfig,
                isSearchModeEnabled = isSearchModeEnabled,
                onToggleSearchMode = {
                    isSearchModeEnabled = it
                },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    @StringRes titleResId: Int,
    onGoBack: () -> Unit,
    actionConfig: ActionConfig? = null,
    searchText: String,
    placeholderText: Int,
    onSearchTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    var isSearchModeEnabled by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            TopBarTitle(
                titleResId = titleResId,
                placeholderText = placeholderText,
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                isSearchModeEnabled = isSearchModeEnabled,
                onToggleSearchMode = { isSearchModeEnabled = it },
                onClearClick = onClearClick,
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            AnimatedVisibility(
                visible = !isSearchModeEnabled,
                enter = fadeIn() + expandHorizontally { -it },
                exit = fadeOut() + shrinkHorizontally { -it },
            ) {
                CircleIconButton(
                    circleIconButtonConfig = CircleIconButtonConfig.Filled(
                        onClick = onGoBack,
                        iconResId = R.drawable.ic_top_bar_back,
                    ),
                )
            }
        },
        actions = {
            TopBarActions(
                actionConfig = actionConfig,
                isSearchModeEnabled = isSearchModeEnabled,
                onToggleSearchMode = {
                    isSearchModeEnabled = it
                },
            )
        },
    )
}

@Composable
private fun TopBarTitle(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    isSearchModeEnabled: Boolean,
    titleResId: Int,
    placeholderText: Int,
    onToggleSearchMode: (Boolean) -> Unit,
    onClearClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.padding(end = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = !isSearchModeEnabled,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            androidx.compose.material3.Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = titleResId),
            )
        }

        AnimatedVisibility(
            visible = isSearchModeEnabled,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SearchTextField(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                placeholderText = placeholderText,
                onToggleSearchMode = onToggleSearchMode,
                onClearClick = onClearClick,
            )

            LaunchedEffect(isSearchModeEnabled) {
                if (isSearchModeEnabled) {
                    focusRequester.requestFocus()
                    inputService?.show()
                }
            }
        }
    }
}

@Composable
private fun TopBarActions(
    actionConfig: ActionConfig? = null,
    isSearchModeEnabled: Boolean,
    onToggleSearchMode: (Boolean) -> Unit,
) {
    Row {
        AnimatedVisibility(
            visible = !isSearchModeEnabled,
            enter = fadeIn() + expandHorizontally { -it },
            exit = fadeOut() + shrinkHorizontally { -it },
        ) {
            CircleIconButton(
                circleIconButtonConfig = CircleIconButtonConfig.Filled(
                    onClick = { onToggleSearchMode(true) },
                    iconResId = R.drawable.ic_search,
                ),
            )
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
    }
}

@Composable
private fun SearchTextField(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    placeholderText: Int,
    onToggleSearchMode: (Boolean) -> Unit,
    onClearClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(min = 40.dp, max = 60.dp)
            .padding(horizontal = 6.dp)
            .focusRequester(focusRequester),
        value = searchText,
        onValueChange = onSearchTextChanged,
        textStyle = MaterialTheme.typography.labelLarge,
        placeholder = {
            androidx.compose.material3.Text(
                text = stringResource(id = placeholderText),
                style = MaterialTheme.typography.labelLarge,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            Icon(
                iconResId = R.drawable.ic_clear,
                contentDescription = stringResource(id = R.string.icon_search_clear_content_description),
                modifier = Modifier.clickable {
                    onClearClick()
                    inputService?.hide()
                    focusManager.clearFocus()
                    onToggleSearchMode(false)
                },
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            },
        ),
    )
}

@UiKitPreview
@Composable
internal fun SearchTopBarPreview() {
    var searchText by remember {
        mutableStateOf("")
    }

    HealtherThemePreview {
        SearchTopBar(
            titleResId = R.string.placeholder_label,
            onGoBack = { /* no-op */ },
            searchText = searchText,
            onSearchTextChanged = {
                searchText = it
            },
            onClearClick = {},
            placeholderText = R.string.default_nowEmpty_text,
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