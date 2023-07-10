package hardcoder.dev.uikit.components.text.textField

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.text.textField.internal.FilledTextField
import hardcoder.dev.uikit.components.text.textField.internal.OutlinedTextField

@Composable
fun TextField(textFieldConfig: TextFieldConfig) {
    when (textFieldConfig) {
        is TextFieldConfig.Filled -> FilledTextField(
            value = textFieldConfig.value,
            onValueChange = textFieldConfig.onValueChange,
            label = textFieldConfig.label,
            textStyle = textFieldConfig.textStyle,
            multiline = textFieldConfig.multiline,
            minLines = textFieldConfig.minLines,
            maxLines = textFieldConfig.maxLines,
            visualTransformation = textFieldConfig.visualTransformation,
            keyboardOptions = textFieldConfig.keyboardOptions,
            keyboardActions = textFieldConfig.keyboardActions,
            isError = textFieldConfig.isError,
            readOnly = textFieldConfig.readOnly,
            leadingIcon = textFieldConfig.leadingIcon,
            trailingIcon = textFieldConfig.trailingIcon,
            regex = textFieldConfig.regex,
            onFocusChanged = textFieldConfig.onFocusChanged,
        )

        is TextFieldConfig.Outlined -> OutlinedTextField(
            value = textFieldConfig.value,
            onValueChange = textFieldConfig.onValueChange,
            label = textFieldConfig.label,
            textStyle = textFieldConfig.textStyle,
            multiline = textFieldConfig.multiline,
            maxLines = textFieldConfig.maxLines,
            visualTransformation = textFieldConfig.visualTransformation,
            keyboardOptions = textFieldConfig.keyboardOptions,
            keyboardActions = textFieldConfig.keyboardActions,
            isError = textFieldConfig.isError,
            readOnly = textFieldConfig.readOnly,
            leadingIcon = textFieldConfig.leadingIcon,
            trailingIcon = textFieldConfig.trailingIcon,
            regex = textFieldConfig.regex,
        )
    }
}