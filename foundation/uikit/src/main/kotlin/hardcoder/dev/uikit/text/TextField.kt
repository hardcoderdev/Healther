package hardcoder.dev.uikit.text

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    textStyle: TextStyle,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = when {
            regex == null -> value
            regex.matches(value) -> value
            else -> ""
        },
        onValueChange = if (regex == null) onValueChange
        else { text: String ->
            if (regex.matches(text)) {
                onValueChange(text)
            }
        },
        label = if (label == null) {
            null
        } else {
            {
                Text(
                    text = stringResource(id = label),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        visualTransformation = visualTransformation,
        singleLine = multiline.not(),
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        textStyle = textStyle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int? = null,
    textStyle: TextStyle,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    readOnly: Boolean = false,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    regex: Regex? = null,
    onFocusChanged: (FocusState) -> Unit = {}
) {
    var focusState: FocusState? by remember { mutableStateOf(null) }
    val focusRequester = remember { FocusRequester() }

    androidx.compose.material.TextField(
        minLines = minLines,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                focusState = it
                onFocusChanged(it)
            },
        value = when {
            regex == null -> value
            regex.matches(value) -> value
            else -> ""
        },
        onValueChange = if (regex == null) onValueChange
        else { text: String ->
            if (regex.matches(text)) {
                onValueChange(text)
            }
        },
        label = if (label == null) {
            null
        } else {
            {
                Text(
                    text = stringResource(id = label),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        visualTransformation = visualTransformation,
        singleLine = multiline.not(),
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = if (focusState?.hasFocus == true) trailingIcon else null,
        textStyle = textStyle
    )
}