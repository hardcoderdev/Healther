package hardcoder.dev.uikit.text

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.uikit.modifiers.onFocusLost


interface TextFieldInputAdapter<INPUT> {
    fun decodeInput(input: INPUT): String?
    fun encodeInput(input: String): INPUT
}

interface TextFieldValidationAdapter<VALIDATION_RESULT> {
    fun errorMessage(result: VALIDATION_RESULT?): String?
}

@Composable
fun <INPUT> rememberInputAdapter(
    decodeInput: (input: INPUT) -> String?,
    encodeInput: (input: String) -> INPUT
) = remember(decodeInput, encodeInput) {
    object : TextFieldInputAdapter<INPUT> {
        override fun decodeInput(input: INPUT) = decodeInput(input)
        override fun encodeInput(input: String) = encodeInput(input)
    }
}

@Composable
fun <VALIDATION_RESULT> rememberValidationAdapter(
    extractErrorMessage: (result: VALIDATION_RESULT?) -> String?
) = remember(extractErrorMessage) {
    object : TextFieldValidationAdapter<VALIDATION_RESULT> {
        override fun errorMessage(result: VALIDATION_RESULT?) = extractErrorMessage(result)
    }
}

@Composable
fun <VALIDATION_RESULT> rememberValidationResourcesAdapter(
    extractErrorMessage: (result: VALIDATION_RESULT?) -> Int?
): TextFieldValidationAdapter<VALIDATION_RESULT> {
    val context = LocalContext.current
    return rememberValidationAdapter {
        extractErrorMessage(it)?.let(context::getString)
    }
}

private object TextInputAdapter : TextFieldInputAdapter<String> {
    override fun decodeInput(input: String) = input
    override fun encodeInput(input: String) = input
}

private object TextFieldEmptyAdapter : TextFieldValidationAdapter<Nothing> {
    override fun errorMessage(result: Nothing?): String? = null
}

@Composable
fun TextField(
    controller: InputController<String>,
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
) {
    ValidatedTextField(
        controller = controller,
        validationAdapter = TextFieldEmptyAdapter,
        modifier = modifier,
        label = label,
        multiline = multiline,
        minLines = minLines,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        regex = regex
    )
}

@Composable
fun <VALIDATION_RESULT> ValidatedTextField(
    controller: ValidatedInputController<String, VALIDATION_RESULT>,
    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
) {
    ValidatedInputField(
        controller = controller,
        inputAdapter = TextInputAdapter,
        validationAdapter = validationAdapter,
        modifier = modifier,
        label = label,
        multiline = multiline,
        minLines = minLines,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        regex = regex
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <INPUT, VALIDATION_RESULT> ValidatedInputField(
    controller: ValidatedInputController<INPUT, VALIDATION_RESULT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
) {
    val state by controller.state.collectAsState()
    val error = validationAdapter.errorMessage(state.validationResult)
    val keyboardController = LocalSoftwareKeyboardController.current

    val finalKeyboardOptions = keyboardOptions ?: remember {
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    }
    val finalKeyboardActions = keyboardActions ?: remember {
        KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    }

    FilledTextField(
        modifier = modifier.onFocusLost(controller::validate),
        value = inputAdapter.decodeInput(state.input).orEmpty(),
        onValueChange = { controller.changeInput(inputAdapter.encodeInput(it)) },
        isError = error != null,
        label = label,
        multiline = multiline,
        minLines = minLines,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = finalKeyboardOptions,
        keyboardActions = finalKeyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        regex = regex,
    )

    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        ErrorText(
            modifier = Modifier.padding(top = 8.dp),
            text = error ?: ""
        )
    }
}