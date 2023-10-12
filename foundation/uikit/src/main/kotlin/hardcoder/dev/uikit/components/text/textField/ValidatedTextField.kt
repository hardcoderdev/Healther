package hardcoder.dev.uikit.components.text.textField

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.uikit.components.modifier.onFocusLost
import hardcoder.dev.uikit.components.text.ErrorText
import hardcoder.dev.uikit.components.text.textField.internal.FilledTextField
import hardcoder.dev.uikit.components.text.textField.internal.OutlinedTextField

interface TextFieldInputAdapter<INPUT> {
    fun decodeInput(input: INPUT): String?
    fun encodeInput(input: String): INPUT
}

interface TextFieldValidationAdapter<VALIDATION_RESULT> {
    fun extractErrorMessage(result: VALIDATION_RESULT?): String?
}

fun <INPUT> TextFieldInputAdapter(
    decodeInput: (input: INPUT) -> String?,
    encodeInput: (input: String) -> INPUT,
) = object : TextFieldInputAdapter<INPUT> {
    override fun decodeInput(input: INPUT) = decodeInput(input)

    override fun encodeInput(input: String) = encodeInput(input)
}

fun <VALIDATION_RESULT> TextFieldValidationAdapter(
    extractErrorMessage: (result: VALIDATION_RESULT?) -> String?,
) = object : TextFieldValidationAdapter<VALIDATION_RESULT> {
    override fun extractErrorMessage(result: VALIDATION_RESULT?) = extractErrorMessage(result)
}

@Composable
fun <VALIDATION_RESULT> textFieldValidationResourcesAdapter(
    extractErrorMessage: (result: VALIDATION_RESULT?) -> Int?,
): TextFieldValidationAdapter<VALIDATION_RESULT> {
    val context = LocalContext.current
    return TextFieldValidationAdapter {
        extractErrorMessage(it)?.let(context::getString)
    }
}

object TextInputAdapter : TextFieldInputAdapter<String> {
    override fun decodeInput(input: String) = input
    override fun encodeInput(input: String) = input
}

object NumberInputAdapter : TextFieldInputAdapter<Int> {
    override fun decodeInput(input: Int) = input.toString()
    override fun encodeInput(input: String) = input.toIntOrNull() ?: 0
}

enum class TextFieldStyle {
    FILLED, OUTLINED
}

@Composable
fun <INPUT, VALIDATION_RESULT> ValidatedTextField(
    modifier: Modifier = Modifier,
    controller: ValidatedInputController<INPUT, VALIDATION_RESULT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
    textFieldStyle: TextFieldStyle = TextFieldStyle.FILLED,
    textStyle: TextStyle = TextStyle.Default,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
) {
    val state by controller.state.collectAsState()
    val error = validationAdapter.extractErrorMessage(state.validationResult)

    Column(modifier) {
        InputField(
            modifier = Modifier.onFocusLost(controller::validate),
            controller = controller,
            inputAdapter = inputAdapter,
            label = label,
            multiline = multiline,
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = textStyle,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            regex = regex,
            textFieldStyle = textFieldStyle,
        )

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            ErrorText(
                modifier = Modifier.padding(top = 8.dp),
                text = error ?: "",
            )
        }
    }
}

@Composable
fun <INPUT> TextField(
    modifier: Modifier = Modifier,
    controller: InputController<INPUT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    textFieldStyle: TextFieldStyle = TextFieldStyle.FILLED,
    textStyle: TextStyle = TextStyle.Default,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
    isError: Boolean = false,
) {
    InputField(
        textFieldStyle = textFieldStyle,
        controller = controller,
        inputAdapter = inputAdapter,
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        multiline = multiline,
        minLines = minLines,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        regex = regex,
        isError = isError,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <INPUT> InputField(
    modifier: Modifier = Modifier,
    controller: InputController<INPUT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    textFieldStyle: TextFieldStyle,
    textStyle: TextStyle = TextStyle.Default,
    @StringRes label: Int? = null,
    multiline: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
    isError: Boolean,
) {
    val state by controller.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val finalKeyboardOptions = keyboardOptions ?: remember {
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        )
    }
    val finalKeyboardActions = keyboardActions ?: remember {
        KeyboardActions(
            onDone = {
                keyboardController?.hide()
            },
        )
    }

    when (textFieldStyle) {
        TextFieldStyle.FILLED -> {
            FilledTextField(
                modifier = modifier.fillMaxWidth(),
                value = inputAdapter.decodeInput(state.input).orEmpty(),
                onValueChange = { controller.changeInput(inputAdapter.encodeInput(it)) },
                label = label,
                isError = isError,
                textStyle = textStyle,
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
        }

        TextFieldStyle.OUTLINED -> {
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = inputAdapter.decodeInput(state.input).orEmpty(),
                onValueChange = { controller.changeInput(inputAdapter.encodeInput(it)) },
                textStyle = textStyle,
                label = label,
                isError = isError,
                multiline = multiline,
                maxLines = maxLines,
                visualTransformation = visualTransformation,
                keyboardOptions = finalKeyboardOptions,
                keyboardActions = finalKeyboardActions,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                regex = regex,
            )
        }
    }
}